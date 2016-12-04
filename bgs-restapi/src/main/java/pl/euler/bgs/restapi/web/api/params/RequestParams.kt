package pl.euler.bgs.restapi.web.api.params

import javaslang.control.Option
import org.springframework.http.HttpMethod

data class ApiHeaders(val userAgent: String, val date: String, val acceptType: String, val agentPasswordHash: String)

data class RequestParams(val url: String, val httpMethod : HttpMethod, val scheme: String, val headers: ApiHeaders, private val _urlParams: Option<String>) {
    val urlParams: String
        get() = _urlParams.orElse("")
}

