package pl.euler.bgs.restapi.web.common

import com.fasterxml.jackson.annotation.JsonRawValue
import com.fasterxml.jackson.annotation.JsonValue

/**
 * Object which could be returned as body response and will be transformed to JSON object from String source.
 */
@Suppress("unused")
class JsonResponse(private val _json: String) {
    val json: String
        @JsonRawValue @JsonValue get() = _json
}