package pl.euler.bgs.restapi.web.api.params;

import com.google.common.base.MoreObjects;
import javaslang.control.Option;
import org.springframework.http.HttpMethod;

import java.util.Objects;

public class RequestParams {
    private final String url;
    private final HttpMethod httpMethod;
    private final String scheme;
    private final ApiHeaders headers;
    private final Option<String> _urlParams;

    public RequestParams(String url, HttpMethod httpMethod, String scheme, ApiHeaders headers, Option<String> urlParams) {
        this.url = url;
        this.httpMethod = httpMethod;
        this.scheme = scheme;
        this.headers = headers;
        _urlParams = urlParams;
    }

    public String getUrl() {
        return url;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public String getScheme() {
        return scheme;
    }

    public ApiHeaders getHeaders() {
        return headers;
    }

    public String getUrlParams() {
        return _urlParams.orElse("");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        RequestParams that = (RequestParams) o;
        return Objects.equals(url, that.url) &&
                httpMethod == that.httpMethod &&
                Objects.equals(scheme, that.scheme) &&
                Objects.equals(headers, that.headers) &&
                Objects.equals(_urlParams, that._urlParams);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, httpMethod, scheme, headers, _urlParams);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("url", url)
                .add("httpMethod", httpMethod)
                .add("scheme", scheme)
                .add("headers", headers)
                .add("_urlParams", _urlParams)
                .toString();
    }
}
