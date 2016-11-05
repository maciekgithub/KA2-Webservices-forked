package pl.euler.bgs.restapi;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.client.RestTemplate;
import pl.euler.bgs.restapi.web.genapi.SubscriptionList;

import static org.assertj.core.api.Assertions.assertThat;

//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
//@TestPropertySource(locations="classpath:test.properties")
public class GenapiInternalApiTest {

    @Test
    @Ignore
    public void shouldExecutePostRequest() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8081/geo/v2/msisdns/{msisdn}/subscriptions";
        ResponseEntity<SubscriptionList> response = restTemplate.getForEntity(url, SubscriptionList.class, "m1");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldCheckMatching() throws Exception {
        MockHttpServletRequest post = new MockHttpServletRequest("POST", "/api/lists/xyz");
        boolean matches = new AntPathRequestMatcher("/lists/{abonent_listname}").matches(post);
        System.out.println(matches);
    }
}
