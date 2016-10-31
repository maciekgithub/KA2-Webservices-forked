package pl.euler.bgs.restapi.web.genapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Controller which has re-mapping for genapi services in order to use as internal api for db calls (due to the fact
 * that there is a problem with oracle and certificates calls).
 */
//@RestController // disabled for now (unused) - remove in future
@SuppressWarnings("unused")
@RequestMapping("/proxy/gen")
public class GenapiProxyController {
  private static final Logger log = LoggerFactory.getLogger(GenapiProxyController.class);

  @Autowired GenapiService genapiService;

  @PostMapping(value = "/v2/msisdns/subscriptions")
  public NewSubscriptionCreated activateSubscription(@RequestBody ActivateSubscription activateSubscription) {
    return genapiService.activateSubscription(activateSubscription);
  }

  @DeleteMapping("/v2/msisdns/{msisdn}/subscriptions/{client_id}")
  public DeletedSubscription deactivateSubscription(@PathVariable String msisdn, @PathVariable("client_id") String clientId) {
    return genapiService.deactivateSubscription(msisdn, clientId);
  }

  @GetMapping(value = "/v2/msisdns/{msisdn}/subscriptions")
  public SubscriptionList getSubscriptions(@PathVariable String msisdn) {
    return genapiService.getSubscriptions(msisdn);
  }

  @GetMapping(value = "/v2/msisdns/{msisdn}/subscriptions/{client_id}")
  public SubscriptionExist isSubscriptionExist(@PathVariable String msisdn, @PathVariable("client_id") String clientId) {
    return genapiService.isSubscriptionExist(msisdn, clientId);
  }

  @GetMapping(value = "/v2/subscriptions/{client_id}")
  public MsisdnList getMsisdnsForSubscription(@PathVariable("client_id") String clientId) {
    return genapiService.getMsisdnsForSubscription(clientId);
  }

  @GetMapping(value = "/v2/msisdns/subscriptions/{client_id}/teryt/{teryt}")
  public MsisdnList getMsisdnsForSubscriptionByArea(@PathVariable("client_id") String clientId, @PathVariable String teryt) {
    return genapiService.getMsisdnsForSubscriptionByArea(clientId, teryt);
  }

}
