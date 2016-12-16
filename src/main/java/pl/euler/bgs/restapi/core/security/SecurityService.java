package pl.euler.bgs.restapi.core.security;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Service;
import pl.euler.bgs.restapi.core.acl.AgentsRepository;
import pl.euler.bgs.restapi.core.acl.EndpointsRepository;
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
    private final AgentsRepository agentsRepository;
    private final EndpointsRepository endpointsRepository;

    @Autowired
    public SecurityService(SecurityProperties securityProperties,
            AgentsRepository agentsRepository, EndpointsRepository endpointsRepository) {
        this.securityProperties = securityProperties;
        this.agentsRepository = agentsRepository;
        this.endpointsRepository = endpointsRepository;
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

        Optional<Agent> optionalAgent = agentsRepository.getAgentDetails(headers.getAgent().getAgentName());
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
        Collection<Endpoint> endpoints = endpointsRepository.getAllAgentEndpoints().get(agent.getName());

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

}
