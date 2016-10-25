package pl.euler.bgs.restapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.euler.bgs.restapi.config.AppProperties;
import pl.euler.bgs.restapi.web.genapi.ActivateSubscription;
import pl.euler.bgs.restapi.web.genapi.GenapiService;
import pl.euler.bgs.restapi.web.genapi.MsisdnList;
import pl.euler.bgs.restapi.web.genapi.NewSubscriptionCreated;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ApplicationTests {

	@Autowired
	AppProperties properties;

	@Autowired
	ObjectMapper objectMapper;

	@Ignore
	@Test
	public void shouldCreateNewSubscription() {
		GenapiService service = new GenapiService(properties, objectMapper);
		NewSubscriptionCreated resp1 = service.activateSubscription(new ActivateSubscription("m1", "c1"));
//		SubscriptionList resp = service.getSubscriptions("m1");
//		DeletedSubscription resp = service.deactivateSubscription("m1", "c2");
//		SubscriptionExist resp = service.isSubscriptionExist("m1", "c2");
		MsisdnList resp = service.getMsisdnsForSubscription("c1");
		System.out.println(resp);

	}

}
