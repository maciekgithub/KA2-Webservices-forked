package pl.euler.genapi

class Subscription(val msisdn: String) {
    val clients: MutableSet<String> = mutableSetOf()

    fun addClient(clientId: String): Boolean = clients.add(clientId)

    fun deleteClient(clientId: String): Boolean = clients.remove(clientId)

    fun hasClient(clientId: String) = clients.contains(clientId)

    override fun toString(): String {
        return "{ $msisdn - $clients }"
    }
}