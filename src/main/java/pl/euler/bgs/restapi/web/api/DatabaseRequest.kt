package pl.euler.bgs.restapi.web.api

import com.fasterxml.jackson.databind.JsonNode
import javaslang.control.Option
import org.apache.commons.lang3.StringUtils
import pl.euler.bgs.restapi.web.api.params.RequestParams

data class DatabaseRequest(val params: RequestParams, private val _requestJson: Option<JsonNode>) {
    val requestJson : String
        get() = _requestJson.map { it.toString() }.orElse("{}")

    constructor(requestParams: RequestParams) :
        this(requestParams, Option.none()) {
    }

    override fun toString(): String {
        return "DatabaseResponse(requestParams=$params, json='$requestJson')"
    }

    fun infoLog(): String = "DatabaseResponse(requestParams=$params, partialJson="+ StringUtils.left(requestJson, 50) +"...)"
}