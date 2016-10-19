package pl.euler.bgs.restapi.web.genapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import javaslang.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.euler.bgs.restapi.config.AppProperties;
import pl.euler.bgs.restapi.core.ssl.TrustAllSslConnectionFactory;

import java.util.function.Function;

@Service
public class GenapiService {
    private static final Logger log = LoggerFactory.getLogger(GenapiService.class);

    private AppProperties appProperties;
    private ObjectMapper objectMapper;
    private RestTemplate restTemplate;

    @Autowired
    public GenapiService(AppProperties properties, ObjectMapper objectMapper) {
        this.appProperties = properties;
        this.objectMapper = objectMapper;
        this.restTemplate = TrustAllSslConnectionFactory.setupRestTemplate();
    }

    public NewSubscriptionCreated activateSubscription(ActivateSubscription activateSubscription) {
        String json = Try
                .of(() -> objectMapper.writeValueAsString(activateSubscription))
                .orElseThrow((Function<Throwable, RuntimeException>) throwable -> new IllegalArgumentException("Cannot prepare json!"));

        ResponseEntity<NewSubscriptionCreated> response =
                restTemplate.postForEntity(getUrl("/msisdns/subscriptions"), json, NewSubscriptionCreated.class);
        log.info("Request for activate subscription. MSISDN:{}, ClientId:{}. Response status:{}, Result:{}",
                 activateSubscription, response.getStatusCode(), response.getBody());
        return response.getBody();
    }

    private String getUrl(String path) {
        return appProperties.getGenapi().getUrl() + path;
    }

    public DeletedSubscription deactivateSubscription(String msisdn, String clientId) {
        ResponseEntity<DeletedSubscription> response = restTemplate.exchange(getUrl("/msisdns/{msisdn}/subscriptions/{client_id}"),
                                                                             HttpMethod.DELETE, null, DeletedSubscription.class,
                                                                             msisdn, clientId);
        log.info("Request for deactivate subscription. MSISDN:{}, ClientId:{}. Response status:{}, Result:{}",
                 msisdn, clientId, response.getStatusCode(), response.getBody());
        return response.getBody();
    }

    public SubscriptionList getSubscriptions(String msisdn) {
        ResponseEntity<SubscriptionList> response = restTemplate.getForEntity(getUrl("/msisdns/{msisdn}/subscriptions"),
                                                                               SubscriptionList.class, msisdn);
        log.info("Request for get subscription. MSISDN:{}, Response status:{}, Result:{}",
                 msisdn, response.getStatusCode(), response.getBody());
        return response.getBody();
    }

    public SubscriptionExist isSubscriptionExist(String msisdn, String clientId) {
        ResponseEntity<SubscriptionExist> response = restTemplate.getForEntity(getUrl("/msisdns/{msisdn}/subscriptions/{client_id}"),
                                                                                SubscriptionExist.class, msisdn, clientId);
        log.info("Does subscription exist. MSISDN:{}, ClientId:{}. Response status:{}, Result:{}",
                 msisdn, clientId, response.getStatusCode(), response.getBody());
        return response.getBody();
    }

    public MsisdnList getMsisdnsForSubscription(String clientId) {
        ResponseEntity<MsisdnList> response = restTemplate.getForEntity(getUrl("/subscriptions/{client_id}"), MsisdnList.class,
                                                                         clientId);
        log.info("Get MSISDNS for subscription. ClientId:{}. Response status:{}, Result:{}",
                 clientId, response.getStatusCode(), response.getBody());
        return response.getBody();
    }

    public MsisdnList getMsisdnsForSubscriptionByArea(String clientId, String area) {
        ResponseEntity<MsisdnList> response = restTemplate.getForEntity(getUrl("/msisdns/subscriptions/{client_id}/teryt/{teryt}"),
                                                                        MsisdnList.class,
                                                                        clientId, area);
        log.info("Get MSISDNS for subscription by teryt. ClientId:{}. Teryt: {} Response status:{}, Result:{}",
                 clientId, area, response.getStatusCode(), response.getBody());
        return response.getBody();
    }

}
