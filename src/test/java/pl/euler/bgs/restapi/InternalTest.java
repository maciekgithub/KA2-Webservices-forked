package pl.euler.bgs.restapi;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.junit.Test;

public class InternalTest {

    @Test
    public void shouldCheckMatching() throws Exception {
        Multimap<String, String> agentsEndpoints = HashMultimap.create();
        System.out.println(agentsEndpoints.get("test"));
    }
}
