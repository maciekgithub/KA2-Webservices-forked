package pl.euler.bgs.restapi.web.api

import org.apache.commons.lang3.StringUtils
import pl.euler.bgs.restapi.web.api.params.RequestParams

data class DatabaseRequest(val requestUrl: String, val params: RequestParams, val requestJson: String) {

    constructor(requestUrl: String, requestParams: RequestParams) :
        this(requestUrl, requestParams, "{}") {
    }

    override fun toString(): String {
        return "DatabaseResponse(requestUrl=$requestUrl, requestParams=$params, json='$requestJson')"
    }

    fun infoLog(): String = "DatabaseResponse(requestUrl=$requestUrl, requestParams=$params, partialJson="+ StringUtils.left(requestJson, 50) +"...)"
}