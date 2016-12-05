package pl.euler.bgs.restapi.core.security;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import pl.euler.bgs.restapi.web.api.Endpoint;
import pl.euler.bgs.restapi.web.api.params.AgentNameAndPassword;
import pl.euler.bgs.restapi.web.api.params.ApiHeaders;
import pl.euler.bgs.restapi.web.api.params.RequestParams;

import java.util.Collection;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
public class SecurityService {
    private static final Logger log = LoggerFactory.getLogger(SecurityService.class);
    private static final String HTTPS_PROTOCOL = "https";

    private final SecurityProperties securityProperties;
    private final JdbcTemplate jdbcTemplate;

    private final Multimap<String, Endpoint> agentsEndpoints;

    @Autowired
    public SecurityService(SecurityProperties securityProperties, JdbcTemplate jdbcTemplate) {
        this.agentsEndpoints = HashMultimap.create();
        this.securityProperties = securityProperties;
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Authorization and authentication of provided agent.
     *
     * @param requestParams params of the request
     * @param currentEndpoint matched generic endpoint
     */
    public void authenticate(RequestParams requestParams, Endpoint currentEndpoint) {
        ApiHeaders headers = requestParams.getHeaders();

        if (isMaintenanceAgent(headers.getAgent())) {
            log.info("Access to generic api by maintenance agent!");
            return;
        }

        Optional<Agent> optionalAgent = getAgentDetails(headers.getAgent().getAgentName());
        Agent agent = optionalAgent.orElseThrow(() -> new AgentAuthenticationException(UNAUTHORIZED, "The provided agent doesn't exist."));

        SecurityRequest securityRequest = createSecurityRequest(requestParams);
        AgentSecurityStatus agentAuthResult = authenticateAgent(agent, securityRequest);

        switch (agentAuthResult) {
            case INCORRECT_PASSWORD:
                throw new AgentAuthenticationException(UNAUTHORIZED, "Authentication failed. Check credentials.");
            case SSL_REQUIRED:
                throw new AgentAuthenticationException(UNAUTHORIZED, "Secure communication is required.");
        }

        if (!isAgentAuthorizedToInvokeEndpoint(agent, currentEndpoint)) {
            throw new AgentAuthenticationException(FORBIDDEN, "Agent has no access to this endpoint.");
        }
    }

    private boolean isMaintenanceAgent(AgentNameAndPassword agent) {
        return StringUtils.equals(agent.getAgentName(), securityProperties.getUser().getName())
                && StringUtils.equals(agent.getPassword(), securityProperties.getUser().getPassword());
    }

    private SecurityRequest createSecurityRequest(RequestParams requestParams) {
        return new SecurityRequest(requestParams.getScheme(), requestParams.getHeaders().getAgent().getHashedPassword());
    }

    private boolean isAgentAuthorizedToInvokeEndpoint(Agent agent, Endpoint endpoint) {
        Collection<Endpoint> endpoints = getAllAgentEndpoints().get(agent.getName());

        return endpoints
                .stream()
                .anyMatch(e -> e.getUrl().equals(endpoint.getUrl()));
    }

    private AgentSecurityStatus authenticateAgent(Agent agent, SecurityRequest securityRequest) {
        requireNonNull(agent);

        if (!agent.getPasswordHash().equals(securityRequest.getPasswordHash())) {
            return AgentSecurityStatus.INCORRECT_PASSWORD;
        }

        boolean sslRequest = HTTPS_PROTOCOL.equals(securityRequest.getSchema());
        if (agent.getSslRequired() != sslRequest) {
            return AgentSecurityStatus.SSL_REQUIRED;
        }

        return AgentSecurityStatus.SUCCESS;
    }

    public Optional<Agent> getAgentDetails(String name) {
        String sql = "SELECT agent_name, auth_password, incoming_ssl, enabled FROM bgs_webservices.wbs$agents WHERE agent_name = ?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new Object[]{name}, (rs, rowNum) -> {
                String agentName = rs.getString("agent_name");
                String passwordHash = rs.getString("auth_password");
                Boolean sslRequired = !rs.getString("incoming_ssl").toUpperCase().equals("N");
                Boolean enabled = !rs.getString("enabled").toUpperCase().equals("N");

                return new Agent(agentName, passwordHash, sslRequired, enabled);
            }));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Multimap<String, Endpoint> getAllAgentEndpoints() {
        if (agentsEndpoints.isEmpty()) {
            String sql =
                "SELECT acl.agent_name as agent_name, e.request_type , e.request_method, e.url"
                + " FROM bgs_webservices.wbs$endpoint_acl acl JOIN bgs_webservices.wbs$endpoints e"
                + " ON acl.request_type = e.request_type"
                + " WHERE acl.enabled = 'Y' AND e.enabled = 'Y'"
                + " ORDER BY acl.agent_name ASC, e.url ASC";

            jdbcTemplate.query(sql, (rs, rowNum) -> {
                String agentName = rs.getString("agent_name");
                String requestType = rs.getString("request_type");
                String requestMethod = rs.getString("request_method");
                String url = rs.getString("url");

                Endpoint endpoint = new Endpoint(requestType, HttpMethod.resolve(requestMethod.toUpperCase()), url);
                agentsEndpoints.put(agentName, endpoint);
                return endpoint;
            });
        }
        return this.agentsEndpoints;
    }

}
