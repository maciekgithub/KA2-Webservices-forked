package pl.euler.bgs.restapi.core.acl;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.euler.bgs.restapi.config.CacheConfiguration;
import pl.euler.bgs.restapi.web.api.Endpoint;

import java.util.Collection;

import static org.apache.commons.lang3.StringUtils.trim;

@Repository
public class EndpointsRepository {
    private static final Logger log = LoggerFactory.getLogger(EndpointsRepository.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public EndpointsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Cacheable(CacheConfiguration.CACHE_AGENTS_ENDPOINTS)
    public Multimap<String, Endpoint> getAllAgentEndpoints() {
        log.info("Loading all agent endpoints from db.");
        Multimap<String, Endpoint> agentsEndpoints = HashMultimap.create();
        String sql =
                "SELECT acl.agent_name as agent_name, e.request_type , e.request_method, e.url"
                        + " FROM bgs_webservices.WBS_ENDPOINT_ACL acl JOIN bgs_webservices.wbs_endpoints e"
                        + " ON acl.request_type = e.request_type"
                        + " WHERE acl.enabled = 'Y' AND e.enabled = 'Y'"
                        + " ORDER BY acl.agent_name ASC, e.url ASC";

        jdbcTemplate.query(sql, (rs, rowNum) -> {
            String agentName = rs.getString("agent_name");
            String requestType = rs.getString("request_type");
            String requestMethod = rs.getString("request_method");
            String url = rs.getString("url");

            Endpoint endpoint = new Endpoint(requestType, HttpMethod.valueOf(requestMethod.toUpperCase()), url);
            agentsEndpoints.put(agentName, endpoint);
            return endpoint;
        });
        return agentsEndpoints;
    }

    @Cacheable(CacheConfiguration.CACHE_REGISTERED_ENDPOINTS)
    public Collection<Endpoint> getRegisteredEndpoints() {
        log.info("Loading all registered endpoints from db.");
        return jdbcTemplate.query("select request_type, request_method, url from bgs_webservices.wbs_endpoints", (rs, rowNum) -> {
            String requestType = rs.getString("request_type");
            String requestMethod = trim(rs.getString("request_method").toUpperCase());
            String url = trim(rs.getString("url"));
            return new Endpoint(requestType, HttpMethod.valueOf(requestMethod), url);
        });
    }

}
