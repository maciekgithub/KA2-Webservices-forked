package pl.euler.bgs.restapi.web.genapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SuppressWarnings("unused")
public class GenapiInvocationController {
  private static final Logger log = LoggerFactory.getLogger(GenapiInvocationController.class);

  @Autowired GenapiService genapiService;

  @GetMapping(value = "/activateSubscription/{msisdn}/{clientId}")
  public NewSubscriptionCreated activateSubscription(@PathVariable String msisdn, @PathVariable String clientId) {
    return genapiService.activateSubscription(msisdn, clientId);
  }

  @GetMapping(value = "/deactivateSubscription/{msisdn}/{clientId}")
  public DeletedSubscription deactivateSubscription(@PathVariable String msisdn, @PathVariable String clientId) {
    return genapiService.deactivateSubscription(msisdn, clientId);
  }

  @GetMapping(value = "/subscriptions/{msisdn}")
  public SubscriptionList getSubscriptions(@PathVariable String msisdn) {
    return genapiService.getSubscriptions(msisdn);
  }

  @GetMapping(value = "/isSubscriptionExist/{msisdn}/{clientId}")
  public SubscriptionExist isSubscriptionExist(@PathVariable String msisdn, @PathVariable String clientId) {
    return genapiService.isSubscriptionExist(msisdn, clientId);
  }

  @GetMapping(value = "/msisdns/{clientId}")
  public MsisdnList getMsisdnsForSubscription(@PathVariable String clientId) {
    return genapiService.getMsisdnsForSubscription(clientId);
  }

  @GetMapping(value = "/msisdnsByArea/{clientId}/{area}")
  public MsisdnList getMsisdnsForSubscriptionByArea(@PathVariable String clientId, @PathVariable String area) {
    return genapiService.getMsisdnsForSubscriptionByArea(clientId, area);
  }

}
