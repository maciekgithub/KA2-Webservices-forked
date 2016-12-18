package pl.euler.bgs.restapi.web.api;

import com.google.common.base.MoreObjects;
import javaslang.control.Option;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.euler.bgs.restapi.web.common.JsonRawResponse;

public class DatabaseResponse {
    private final Option<String> json;
    private final int statusCode;

    public DatabaseResponse(Option<String> json, int statusCode) {
        this.json = json;
        this.statusCode = statusCode;
    }

    public Option<String> getJson() {
        return json;
    }

    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Immediately convert db result to proper [ResponseEntity].

     * @return response entity with generic type [JsonRawResponse]
     */
    @SuppressWarnings("unchecked")
    public ResponseEntity<JsonRawResponse> convertToWebResponse() {
        return json.map( it ->
            new ResponseEntity(new JsonRawResponse(it), HttpStatus.valueOf(statusCode))
        ).orElse(
                new ResponseEntity(HttpStatus.valueOf(statusCode))
        );
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("statusCode", statusCode)
                .add("json", json)
                .toString();
    }

    public String infoLog(){
        return "DatabaseResponse(statusCode=" + statusCode + ", partialJson=" + StringUtils.left(json.orElse("EMPTY"), 50) + "...)";
    }

}
