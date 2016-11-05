package pl.euler.bgs.restapi.web.api

import org.springframework.http.HttpMethod

data class Endpoint(val requestType: String, val httpMethod : HttpMethod, val url : String)