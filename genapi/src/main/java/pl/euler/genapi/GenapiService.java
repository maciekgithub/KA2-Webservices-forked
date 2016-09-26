package pl.euler.genapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static java.util.stream.Collectors.toSet;

public class GenapiService {
	private static final Logger log = LoggerFactory.getLogger(GenapiService.class);

	public NewSubscriptionCreated activateSubscription(String msisdn, String clientId) {
		Subscription subscription = getOrCreateSubscription(msisdn);
		NewSubscriptionCreated response = new NewSubscriptionCreated(subscription.addClient(clientId));
        updateOnDb(subscription);
		log.info("Activate subscription {} on msisdn {}", clientId, msisdn);
        displayDbState();
		return response;
	}

	public DeletedSubscription deactivateSubscription(String msisdn, String clientId) {
        Subscription subscription = getOrCreateSubscription(msisdn);
        DeletedSubscription deletedSubscription = new DeletedSubscription(subscription.deleteClient(clientId));
        log.info("Deactivate subscription {} on msisdn {}", clientId, msisdn);
        displayDbState();
        return deletedSubscription;
    }

    public SubscriptionList getSubscriptions(String msisdn) {
        Subscription subscription = getOrCreateSubscription(msisdn);
        log.info("Get subscriptions for msisdn: {}", msisdn);
        displayDbState();
        return new SubscriptionList(subscription.getClients());
    }

    public SubscriptionExist isSubscriptionExist(String msisdn, String clientId) {
        Subscription subscription = getOrCreateSubscription(msisdn);
        log.info("Is subscription {} exist for msisdn {}", clientId, msisdn);
        displayDbState();
        return new SubscriptionExist(subscription.hasClient(clientId));
    }

    public MsisdnList getMsisdnsForSubscription(String clientId) {
        log.info("Get msisdns for subscription {}", clientId);
        displayDbState();
        return new MsisdnList(findAll().stream().filter(s -> s.hasClient(clientId)).map(Subscription::getMsisdn).collect(toSet()));
    }

    public MsisdnList getMsisdnsForSubscriptionByArea(String clientId, String area) {
        log.info("Get msisdns for subscription {} in area {}", clientId, area);
        return getMsisdnsForSubscription(clientId); // just mock to return data
    }

    // DB

	private ConcurrentMap<String, Subscription> MSISDN_DB = new ConcurrentHashMap<>();

    private Collection<Subscription> findAll() {
        return MSISDN_DB.values();
    }

    private Subscription getOrCreateSubscription(String msisdn) {
		return MSISDN_DB.getOrDefault(msisdn, new Subscription(msisdn));
	}

	private void updateOnDb(Subscription subscription) {
		MSISDN_DB.put(subscription.getMsisdn(), subscription);
    }

    private void displayDbState() {
        log.info("current db state: {}", MSISDN_DB.values());
    }

}
