package pl.euler.bgs.restapi;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.junit.Test;

//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
//@TestPropertySource(locations="classpath:test.properties")
public class InternalTest {

    @Test
    public void shouldCheckMatching() throws Exception {
        Multimap<String, String> agentsEndpoints = HashMultimap.create();
        System.out.println(agentsEndpoints.get("test"));
    }
}
