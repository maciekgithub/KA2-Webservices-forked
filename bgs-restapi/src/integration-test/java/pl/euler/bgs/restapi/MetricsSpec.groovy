package pl.euler.bgs.restapi

import groovyx.net.http.RESTClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.env.Environment
import org.springframework.test.context.TestPropertySource
import spock.lang.Shared
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations="classpath:test.properties")
class MetricsSpec extends Specification {

    @Autowired
    Environment environment;

    @Autowired
    TestRestTemplate restTemplate;

    @Shared
    def client = new RESTClient("http://localhost:8052")

//    @Test
    def "shouldCreateNewSubscription"() {
        when:
        def resp = client.get(path: '/api/dictionaries', headers: ["User-Agent": "kapitan", "Date": "2012"])
        println resp.status
        println resp.data
        System.out.println(Arrays.toString(environment.getActiveProfiles()));
//		String response = restTemplate.getForObject("/management/health", String.class);
        then:
        String response = restTemplate.getForObject("/api/dictionaries", String.class);
        System.out.println(response);
    }

}