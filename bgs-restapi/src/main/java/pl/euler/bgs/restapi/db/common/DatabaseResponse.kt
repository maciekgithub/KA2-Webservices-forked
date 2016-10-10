package pl.euler.bgs.restapi.db.common

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import pl.euler.bgs.restapi.web.common.JsonResponse

/**
 * Result of data processing by database.
 */
class DatabaseResponse(val json: String, val statusCode: Int) {

    /**
     * Immediately convert db result to proper [ResponseEntity].

     * @return response entity with generic type [JsonResponse]
     */
    fun convertToWebResponse(): ResponseEntity<JsonResponse> {
        return ResponseEntity(JsonResponse(json), HttpStatus.valueOf(statusCode))
    }

}
