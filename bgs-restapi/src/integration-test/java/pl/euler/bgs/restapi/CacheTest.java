package pl.euler.bgs.restapi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import pl.euler.bgs.restapi.core.acl.AgentsRepository;
import pl.euler.bgs.restapi.core.management.MaintenanceService;
import pl.euler.bgs.restapi.core.security.Agent;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(locations="classpath:integration-test.properties")
public class CacheTest {

    @Autowired
    AgentsRepository agentsRepository;

    @Autowired
    MaintenanceService maintenanceService;

    @Test
    public void shouldCacheSelectedAgent() {
        //when
        Optional<Agent> genAgentDetails = agentsRepository.getAgentDetails("GEN");
        Optional<Agent> genAgentDetailsCached = agentsRepository.getAgentDetails("GEN");

        assert genAgentDetails.isPresent();
        assert genAgentDetailsCached.isPresent();

        //then
        assertThat(genAgentDetails.get()).isSameAs(genAgentDetailsCached.get());
    }

    @Test
    public void shouldClearAllCaches() {
        //given
        Optional<Agent> genAgentDetails = agentsRepository.getAgentDetails("GEN");

        //when
        maintenanceService.clearAllCaches();
        Optional<Agent> genAgentDetailsCached = agentsRepository.getAgentDetails("GEN");

        //then
        assert genAgentDetails.isPresent();
        assert genAgentDetailsCached.isPresent();
        assertThat(genAgentDetails.get()).isNotSameAs(genAgentDetailsCached.get());
    }

}
