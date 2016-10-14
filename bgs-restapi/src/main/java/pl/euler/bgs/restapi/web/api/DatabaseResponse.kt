package pl.euler.bgs.restapi.web.api

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import pl.euler.bgs.restapi.web.common.JsonRawResponse

/**
 * Result of data processing by database.
 */
class DatabaseResponse(val json: String, val statusCode: Int) {

    /**
     * Immediately convert db result to proper [ResponseEntity].

     * @return response entity with generic type [JsonRawResponse]
     */
    fun convertToWebResponse(): ResponseEntity<JsonRawResponse> {
        return ResponseEntity(JsonRawResponse(json), HttpStatus.valueOf(statusCode))
    }

}
