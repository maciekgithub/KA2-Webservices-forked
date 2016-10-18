package pl.euler.bgs.restapi.web.genapi

class ActivateSubscription(val client_id: String, val msisdn: String) {
    override fun toString(): String {
        return "activate_subscription, client_id = $client_id, msisdn = $msisdn"
    }
}

class NewSubscriptionCreated(val created_new_subscription: Boolean) {
    override fun toString(): String {
        return "created_new_subscription = " + created_new_subscription
    }
}

class DeletedSubscription(val deleted_subscription: Boolean) {
    override fun toString(): String {
        return "deleted_subscription = " + deleted_subscription
    }
}

class SubscriptionList(val client_id: Set<String>) {
    override fun toString(): String {
        return "client_id = " + client_id
    }
}

class SubscriptionExist(val subscription_exist: Boolean) {
    override fun toString(): String {
        return "subscription_exist = " + subscription_exist
    }
}

class MsisdnList(val msisdns: Set<String>) {
    override fun toString(): String {
        return "msisdns = " +msisdns
    }
}