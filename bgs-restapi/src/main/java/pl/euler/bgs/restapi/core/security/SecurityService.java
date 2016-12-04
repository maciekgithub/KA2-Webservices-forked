package pl.euler.bgs.restapi.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import pl.euler.bgs.restapi.web.api.Endpoint;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Service
public class SecurityService {

    private Map<String, Collection<Endpoint>> agentsEndpoints;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public SecurityService(JdbcTemplate jdbcTemplate) {
        this.agentsEndpoints = new HashMap<>();
        this.jdbcTemplate = jdbcTemplate;
    }

    public Agent getAgentDetails(String name) {
        String sql = "SELECT agent_name, auth_password, incoming_ssl, enabled FROM bgs_webservices.wbs$agents WHERE agent_name = ?";

        return jdbcTemplate.queryForObject(sql, new Object[] {name}, (rs, rowNum) -> {
            String agentName = rs.getString("agent_name");
            String passwordHash = rs.getString("auth_password");
            Boolean sslRequired = rs.getString("incoming_ssl").toUpperCase().equals("N") ? false : true;
            Boolean enabled = rs.getString("enabled").toUpperCase().equals("N") ? false : true;

            return new Agent(agentName, passwordHash, sslRequired, enabled);
        });
    }

    public boolean isAgentAuthorizedToInvokeEndpoint() {

        return false;
    }

    public Map<String, Collection<Endpoint>> getAllAgentEndpoints() {
        if (agentsEndpoints.isEmpty()) {
            Set<Endpoint> endpoints = new HashSet<Endpoint>();

            String sql =
                "SELECT acl.agent_name, e.request_type, e.request_method, e.url"
                + " FROM bgs_webservices.wbs$endpoint_acl acl JOIN bgs_webservices.wbs$endpoints e"
                + " ON acl.request_type = e.request_type"
                + " WHERE acl.enabled = 'Y' AND e.enabled = 'Y'"
                + " ORDER BY acl.agent_name ASC, e.url ASC";

            jdbcTemplate.query(sql, (rs, rowNum) -> {
                String agentName = rs.getString("acl.agent_name");
                String requestType = rs.getString("e.request_type");
                String requestMethod = rs.getString("e.request_method");
                String url = rs.getString("e.url");

                Endpoint endpoint = new Endpoint(requestType, HttpMethod.resolve(requestMethod), url, true);

                if (!agentsEndpoints.containsKey(agentName)) {
                    endpoints.add(endpoint);
                } else {
                    agentsEndpoints.put(agentName, endpoints);
                    endpoints.clear();
                }

                return endpoint;
            });
        }

        return this.agentsEndpoints;
    }

}
