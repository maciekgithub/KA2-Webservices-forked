package pl.euler.bgs.restapi.core.security

data class Agent(val name: String, val passwordHash: String, val sslRequired: Boolean, val enabled: Boolean)