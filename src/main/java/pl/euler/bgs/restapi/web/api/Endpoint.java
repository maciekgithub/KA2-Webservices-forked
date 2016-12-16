package pl.euler.bgs.restapi.web.api;

import com.google.common.base.MoreObjects;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public class Endpoint {
    private final String requestType;
    private final HttpMethod httpMethod;
    private final String url;
    private final Boolean enabled;

    public Endpoint(String requestType, HttpMethod httpMethod, String url, Boolean enabled) {
        this.requestType = requestType;
        this.httpMethod = httpMethod;
        this.url = url;
        this.enabled = enabled;
    }

    public Endpoint(String requestType, HttpMethod httpMethod, String url) {
        this(requestType, httpMethod, url, true);
    }

    public String getRequestType() {
        return requestType;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public String getUrl() {
        return url;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    /**
     * Retrieve endpoint address which is provided to database service.
     */
    public static String getEndpointUrl(HttpServletRequest request) {
        String pathWithinApplication = new UrlPathHelper().getPathWithinApplication(request);
        return StringUtils.removeEnd(pathWithinApplication, "/");
    }

    /**
     * Checks whether provided request ends with slash.
     */

    public static Boolean isPathEndsWithSlash(HttpServletRequest request) {
        String url = request.getServletPath();
        if (request.getPathInfo() != null) {
            url += request.getPathInfo();
        }
        return url.endsWith("/");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Endpoint endpoint = (Endpoint) o;
        return Objects.equals(requestType, endpoint.requestType) &&
                httpMethod == endpoint.httpMethod &&
                Objects.equals(url, endpoint.url) &&
                Objects.equals(enabled, endpoint.enabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestType, httpMethod, url, enabled);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("requestType", requestType)
                .add("httpMethod", httpMethod)
                .add("url", url)
                .add("enabled", enabled)
                .toString();
    }
}
