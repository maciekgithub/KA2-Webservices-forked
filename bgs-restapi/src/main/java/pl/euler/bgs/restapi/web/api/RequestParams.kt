package pl.euler.bgs.restapi.web.api

import org.springframework.http.HttpMethod

data class RequestParams(val httpMethod : HttpMethod, val urlParams: String) {

    constructor(httpMethod: HttpMethod) : this(httpMethod, "")
}