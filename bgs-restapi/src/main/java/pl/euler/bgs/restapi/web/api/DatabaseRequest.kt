package pl.euler.bgs.restapi.web.api

import org.apache.commons.lang3.StringUtils
import pl.euler.bgs.restapi.web.api.headers.ApiHeaders
import pl.euler.bgs.restapi.web.api.params.RequestParams

data class DatabaseRequest(val requestUrl: String, val params: RequestParams, val headers: ApiHeaders, val requestJson: String) {

    constructor(requestUrl: String, requestParams: RequestParams, headers: ApiHeaders) :
        this(requestUrl, requestParams, headers, "{}") {
    }

    override fun toString(): String {
        return "DatabaseResponse(requestUrl=$requestUrl, requestParams=$params, headers=$headers, json='$requestJson')"
    }

    fun infoLog(): String = "DatabaseResponse(requestUrl=$requestUrl, requestParams=$params, headers=$headers, partialJson="+ StringUtils.left(requestJson, 50) +"...)"
}