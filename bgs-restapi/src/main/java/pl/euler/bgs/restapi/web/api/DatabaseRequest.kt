package pl.euler.bgs.restapi.web.api

import org.apache.commons.lang3.StringUtils
import pl.euler.bgs.restapi.web.api.params.RequestParams

data class DatabaseRequest(val params: RequestParams, val requestJson: String) {

    constructor(requestParams: RequestParams) :
        this(requestParams, "{}") {
    }

    override fun toString(): String {
        return "DatabaseResponse(requestParams=$params, json='$requestJson')"
    }

    fun infoLog(): String = "DatabaseResponse(requestParams=$params, partialJson="+ StringUtils.left(requestJson, 50) +"...)"
}