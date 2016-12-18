package pl.euler.bgs.restapi.web.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.MoreObjects;
import javaslang.control.Option;
import org.apache.commons.lang3.StringUtils;
import pl.euler.bgs.restapi.web.api.params.RequestParams;

import java.util.Objects;

public class DatabaseRequest {
    private final RequestParams params;
    private final Option<JsonNode> _requestJson;

    public DatabaseRequest(RequestParams params, Option<JsonNode> requestJson) {
        this.params = params;
        _requestJson = requestJson;
    }

    public DatabaseRequest(RequestParams params) {
        this(params, Option.none());
    }

    public RequestParams getParams() {
        return params;
    }

    public String getRequestJson() {
        return _requestJson.map(JsonNode::toString).orElse("{}");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DatabaseRequest that = (DatabaseRequest) o;
        return Objects.equals(params, that.params) &&
                Objects.equals(_requestJson, that._requestJson);
    }

    @Override
    public int hashCode() {
        return Objects.hash(params, _requestJson);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("params", params)
                .add("requestJson", getRequestJson())
                .toString();
    }

    public String infoLog() {
        return "DatabaseResponse(requestParams=" + params + ", partialJson=" + StringUtils.left(getRequestJson(), 50) + "...)";
    }

}
