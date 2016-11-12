package pl.euler.bgs.restapi.web.api

import org.springframework.http.HttpMethod
import org.springframework.web.util.UrlPathHelper
import pl.euler.bgs.restapi.web.api.params.RequestParamsResolver.API_PREFIX
import pl.euler.bgs.restapi.web.api.params.RequestParamsResolver.GAPI_PREFIX
import javax.servlet.http.HttpServletRequest

data class Endpoint(val requestType: String, val httpMethod : HttpMethod, val url : String) {

    companion object HttpUtils {
        /**
         * Retrieve endpoint address which is provided to database service.
         *
         * The /api and /gapi prefix is removed and the ending slash too
         */
        fun getEndpointUrl(request: HttpServletRequest): String {
            val pathWithinApplication = UrlPathHelper().getPathWithinApplication(request)
            return pathWithinApplication.replaceFirst(API_PREFIX, "").replaceFirst(GAPI_PREFIX, "").removeSuffix("/")
        }

        /**
         * Checks whether provided request ends with slash.
         */
        fun isPathEndsWithSlash(request: HttpServletRequest): Boolean {
            var url = request.servletPath
            if (request.pathInfo != null) {
                url += request.pathInfo
            }
            return url.endsWith("/")
        }
    }
}