package pl.euler.bgs.restapi.web.api

import org.apache.commons.lang3.StringUtils

data class DatabaseRequest(val requestUrl: String, val requestParams: String, val headers: ApiHeaders, val requestJson: String) {
    override fun toString(): String {
        return "DatabaseResponse(requestUrl=$requestUrl, requestParams=$requestParams, headers=$headers, json='$requestJson')"
    }

    fun infoLog(): String = "DatabaseResponse(requestUrl=$requestUrl, requestParams=$requestParams, headers=$headers, partialJson="+ StringUtils.left(requestJson, 50) +"...)"
}