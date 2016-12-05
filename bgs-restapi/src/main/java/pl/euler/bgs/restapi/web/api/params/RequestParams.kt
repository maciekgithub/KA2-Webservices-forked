package pl.euler.bgs.restapi.web.api.params

import javaslang.control.Option
import org.springframework.http.HttpMethod
import org.springframework.util.DigestUtils

data class ApiHeaders(val agent: AgentNameAndPassword, val date: String, val acceptType: String)

data class RequestParams(val url: String, val httpMethod : HttpMethod, val scheme: String, val headers: ApiHeaders, private val _urlParams: Option<String>) {
    val urlParams: String
        get() = _urlParams.orElse("")
}

data class AgentNameAndPassword(val agentName: String, val password: String) {
    fun getHashedPassword(): String = DigestUtils.md5DigestAsHex(password.toByteArray())
}