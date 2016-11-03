package pl.euler.bgs.restapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import pl.euler.bgs.restapi.config.AppProperties;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
@TestPropertySource(locations="classpath:test.properties")
@ActiveProfiles("test")
public class MetricsTest {

	@Autowired
	AppProperties properties;

	@Autowired
	ObjectMapper objectMapper;


	@Autowired
	TestRestTemplate restTemplate;

	@Test
	public void shouldCreateNewSubscription() {
//		String response = restTemplate.getForObject("/management/health", String.class);
		String response = restTemplate.getForObject("/api/dictionaries", String.class);
		System.out.println(response);
	}

}
