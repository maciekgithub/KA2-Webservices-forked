package pl.euler.bgs.restapi.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class GenapiService {
    private static final Logger log = LoggerFactory.getLogger(GenapiService.class);
    private RestTemplate restTemplate = new RestTemplate();

    private GenapiProperties properties;

    @Autowired
    public GenapiService(GenapiProperties properties) {
        this.properties = properties;
    }

    public NewSubscriptionCreated activateSubscription(String msisdn, String clientId) {
        MultiValueMap<String, String> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add("msisdn", msisdn);
        bodyMap.add("client_id", clientId);

        ResponseEntity<NewSubscriptionCreated> response =
                restTemplate.postForEntity(getUrl("/v2/msisdns/subscriptions"), bodyMap, NewSubscriptionCreated.class);
        log.info("Request for activate subscription. MSISDN:{}, ClientId:{}. Response status:{}, Result:{}",
                 msisdn, clientId, response.getStatusCode(), response.getBody());
        return response.getBody();
    }

    private String getUrl(String path) {
        return properties.getUrl() + path;
    }

    public DeletedSubscription deactivateSubscription(String msisdn, String clientId) {
        ResponseEntity<DeletedSubscription> response = restTemplate.exchange(getUrl("/v2/msisdns/{msisdn}/subscriptions/{client_id}"),
                                                                             HttpMethod.DELETE, null, DeletedSubscription.class,
                                                                             msisdn, clientId);
        log.info("Request for deactivate subscription. MSISDN:{}, ClientId:{}. Response status:{}, Result:{}",
                 msisdn, clientId, response.getStatusCode(), response.getBody());
        return response.getBody();
    }

    public SubscriptionList getSubscriptions(String msisdn) {
        ResponseEntity<SubscriptionList> response = restTemplate.getForEntity(getUrl("/v2/msisdns/{msisdn}/subscriptions"),
                                                                               SubscriptionList.class, msisdn);
        log.info("Request for get subscription. MSISDN:{}, Response status:{}, Result:{}",
                 msisdn, response.getStatusCode(), response.getBody());
        return response.getBody();
    }

    public SubscriptionExist isSubscriptionExist(String msisdn, String clientId) {
        ResponseEntity<SubscriptionExist> response = restTemplate.getForEntity(getUrl("/v2/msisdns/{msisdn}/subscriptions/{client_id}"),
                                                                                SubscriptionExist.class, msisdn, clientId);
        log.info("Does subscription exist. MSISDN:{}, ClientId:{}. Response status:{}, Result:{}",
                 msisdn, clientId, response.getStatusCode(), response.getBody());
        return response.getBody();
    }

    public MsisdnList getMsisdnsForSubscription(String clientId) {
        ResponseEntity<MsisdnList> response = restTemplate.getForEntity(getUrl("v2/subscriptions/{client_id}"), MsisdnList.class,
                                                                         clientId);
        log.info("Get MSISDNS for subscription. ClientId:{}. Response status:{}, Result:{}",
                 clientId, response.getStatusCode(), response.getBody());
        return response.getBody();
    }

    public MsisdnList getMsisdnsForSubscriptionByArea(String clientId, String area) {
        ResponseEntity<MsisdnList> response = restTemplate.getForEntity(getUrl("v2/msisdns/subscriptions/{client_id}/teryt/{teryt}"),
                                                                        MsisdnList.class,
                                                                        clientId, area);
        log.info("Get MSISDNS for subscription by teryt. ClientId:{}. Teryt: {} Response status:{}, Result:{}",
                 clientId, area, response.getStatusCode(), response.getBody());
        return response.getBody();
    }

}
