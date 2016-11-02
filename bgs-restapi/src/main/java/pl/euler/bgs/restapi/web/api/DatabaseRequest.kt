package pl.euler.bgs.restapi.web.api

import org.apache.commons.lang3.StringUtils
import org.springframework.http.HttpMethod
import pl.euler.bgs.restapi.web.api.headers.ApiHeaders

data class DatabaseRequest(val requestUrl: String, val requestMethod: HttpMethod, val requestParams: String, val headers: ApiHeaders, val requestJson: String) {

    constructor(requestUrl: String, requestMethod: HttpMethod, headers: ApiHeaders) :
        this(requestUrl, requestMethod, "", headers, "{}") {
    }

    constructor(requestUrl: String, requestMethod: HttpMethod, requestParams: String, headers: ApiHeaders) :
        this(requestUrl, requestMethod, requestParams, headers, "{}") {
    }

    constructor(requestUrl: String, requestMethod: HttpMethod, headers: ApiHeaders, requestJson: String) :
        this(requestUrl, requestMethod, "", headers, requestJson) {
    }

    override fun toString(): String {
        return "DatabaseResponse(requestUrl=$requestUrl, requestMethod=$requestMethod, requestParams=$requestParams, headers=$headers, json='$requestJson')"
    }

    fun infoLog(): String = "DatabaseResponse(requestUrl=$requestUrl, requestMethod=$requestMethod, requestParams=$requestParams, headers=$headers, partialJson="+ StringUtils.left(requestJson, 50) +"...)"
}