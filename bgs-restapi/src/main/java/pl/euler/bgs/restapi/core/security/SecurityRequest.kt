package pl.euler.bgs.restapi.core.security

data class SecurityRequest(val schema: String, val passwordHash: String)