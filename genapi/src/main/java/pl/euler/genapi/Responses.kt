package pl.euler.genapi

class NewSubscriptionCreated(val created_new_subscription: Boolean)

class DeletedSubscription(val deleted_subscription: Boolean)

class SubscriptionList(val client_id: Set<String>)

class SubscriptionExist(val subscription_exist: Boolean)

class MsisdnList(val msisdns: Set<String>)