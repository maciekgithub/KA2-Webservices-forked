package pl.euler.bgs.restapi.web.api.params

import javaslang.control.Option
import org.springframework.http.HttpMethod

data class ApiHeaders(val userAgent: String, val date: String, val acceptType: String) {
    constructor(userAgent: String, date: String, acceptType: String, scheme: String, authorization: String): this(userAgent, date, acceptType)
}

data class RequestParams(val url: String, val httpMethod : HttpMethod, val headers: ApiHeaders, private val _urlParams: Option<String>) {
    val urlParams: String
        get() = _urlParams.orElse("")
}

