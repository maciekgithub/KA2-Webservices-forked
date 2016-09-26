package pl.euler.bgs.restapi;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.euler.bgs.restapi.internal.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ApplicationTests {

	@Autowired GenapiProperties properties;

	@Ignore
	@Test
	public void shouldCreateNewSubscription() {
		GenapiService service = new GenapiService(properties);
		NewSubscriptionCreated resp1 = service.activateSubscription("m1", "c1");
//		SubscriptionList resp = service.getSubscriptions("m1");
//		DeletedSubscription resp = service.deactivateSubscription("m1", "c2");
//		SubscriptionExist resp = service.isSubscriptionExist("m1", "c2");
		MsisdnList resp = service.getMsisdnsForSubscription("c1");
		System.out.println(resp);

	}

}
