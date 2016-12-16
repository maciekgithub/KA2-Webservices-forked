package pl.euler.bgs.restapi.web.api

import javaslang.control.Option
import org.apache.commons.lang3.StringUtils
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import pl.euler.bgs.restapi.web.common.JsonRawResponse

/**
 * Result of data processing by database.
 */
class DatabaseResponse(val json: Option<String>, val statusCode: Int) {

    /**
     * Immediately convert db result to proper [ResponseEntity].

     * @return response entity with generic type [JsonRawResponse]
     */
    fun convertToWebResponse(): ResponseEntity<JsonRawResponse> {
        return json.map {
            ResponseEntity(JsonRawResponse(it), HttpStatus.valueOf(statusCode))
        }.orElse(
            ResponseEntity(HttpStatus.valueOf(statusCode))
        )
    }

    override fun toString(): String {
        return "DatabaseResponse(statusCode=$statusCode, json='$json')"
    }

    fun infoLog(): String = "DatabaseResponse(statusCode=$statusCode, partialJson=" + StringUtils.left(json.orElse("EMPTY"), 50) + "...)"
}
