package pl.euler.bgs.restapi.web.api

import org.apache.commons.lang3.StringUtils
import org.springframework.http.HttpMethod
import pl.euler.bgs.restapi.web.api.headers.ApiHeaders

data class DatabaseRequest(val requestUrl: String, val params: RequestParams, val headers: ApiHeaders, val requestJson: String) {

    constructor(requestUrl: String, requestMethod: HttpMethod, headers: ApiHeaders) :
        this(requestUrl, RequestParams(requestMethod), headers, "{}") {
    }

    constructor(requestUrl: String, requestParams: RequestParams, headers: ApiHeaders) :
        this(requestUrl, requestParams, headers, "{}") {
    }

    constructor(requestUrl: String, requestMethod: HttpMethod, headers: ApiHeaders, requestJson: String) :
        this(requestUrl, RequestParams(requestMethod), headers, requestJson) {
    }

    override fun toString(): String {
        return "DatabaseResponse(requestUrl=$requestUrl, requestParams=$params, headers=$headers, json='$requestJson')"
    }

    fun infoLog(): String = "DatabaseResponse(requestUrl=$requestUrl, requestParams=$params, headers=$headers, partialJson="+ StringUtils.left(requestJson, 50) +"...)"
}