package pl.euler.bgs.restapi.web.api

import org.apache.commons.lang3.StringUtils
import org.springframework.http.HttpMethod

data class DatabaseRequest(val requestUrl: String, val requestMethod: HttpMethod, val requestParams: String, val headers: ApiHeaders, val requestJson: String) {
    override fun toString(): String {
        return "DatabaseResponse(requestUrl=$requestUrl, requestMethod=$requestMethod, requestParams=$requestParams, headers=$headers, json='$requestJson')"
    }

    fun infoLog(): String = "DatabaseResponse(requestUrl=$requestUrl, requestMethod=$requestMethod, requestParams=$requestParams, headers=$headers, partialJson="+ StringUtils.left(requestJson, 50) +"...)"
}