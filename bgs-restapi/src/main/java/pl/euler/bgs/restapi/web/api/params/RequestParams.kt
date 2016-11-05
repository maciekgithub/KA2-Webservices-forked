package pl.euler.bgs.restapi.web.api.params

import org.springframework.http.HttpMethod

data class RequestParams(val httpMethod : HttpMethod, val urlParams: String)