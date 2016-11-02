package pl.euler.bgs.restapi.web.api.headers

data class ApiHeaders(val userAgent: String = "", val date: String = "", val contentType: String = "application/json")