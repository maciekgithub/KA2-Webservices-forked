package pl.euler.bgs.restapi.core.acl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.euler.bgs.restapi.config.CacheConfiguration;
import pl.euler.bgs.restapi.core.security.Agent;

import java.util.Optional;

@Repository
public class AgentsRepository {
    private static final Logger log = LoggerFactory.getLogger(AgentsRepository.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AgentsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Cacheable(value = CacheConfiguration.CACHE_AGENTS, key = "#name")
    public Optional<Agent> getAgentDetails(String name) {
        log.info("Loading agent: {} details.", name);
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

}
