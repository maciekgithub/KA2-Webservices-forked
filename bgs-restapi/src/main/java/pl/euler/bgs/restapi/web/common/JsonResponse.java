package pl.euler.bgs.restapi.web.common;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Object which could be returned as body response and will be transformed to JSON object from String source.
 */
public class JsonResponse {
    private final String json;

    public JsonResponse(String json) {
        this.json = json;
    }

    @JsonValue
    @JsonRawValue
    public String getJson() {
        return json;
    }
}
