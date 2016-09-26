package pl.euler.bgs.restapi.internal

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