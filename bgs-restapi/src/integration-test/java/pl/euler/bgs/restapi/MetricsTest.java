package pl.euler.bgs.restapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import pl.euler.bgs.restapi.config.AppProperties;

import java.util.Arrays;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
@TestPropertySource(locations="classpath:test.properties")
//@ActiveProfiles("test")
public class MetricsTest {

	@Autowired
	AppProperties properties;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	Environment environment;

	@Autowired
	TestRestTemplate restTemplate;

	@Test
	public void shouldCreateNewSubscription() throws Exception {
		System.out.println(Arrays.toString(environment.getActiveProfiles()));
//		String response = restTemplate.getForObject("/management/health", String.class);
//		String response = restTemplate.getForObject("/api/dictionaries", String.class);
		Unirest.setDefaultHeader("Accept", "*/*");
		Unirest.setDefaultHeader("User-Agent", "kapitan");
		Unirest.setDefaultHeader("Date", "2016");
		JsonNode response = Unirest.get("http://localhost:8052/api/dictionaries").asJson().getBody();

//		JSONObject object = Unirest.get("http://localhost:8052/api/dictionaries").asObject(JSONObject.class).getBody();
//		System.out.println(object);
//		System.out.println(response);

		Unirest.shutdown();
	}

}
