package pl.euler.bgs.restapi.web.api

import org.springframework.http.HttpMethod
import org.springframework.web.util.UrlPathHelper
import javax.servlet.http.HttpServletRequest

data class Endpoint(val requestType: String, val httpMethod : HttpMethod, val url : String) {

    companion object {
        /**
         * Retrieve endpoint address which is provided to database service.
         */
        @JvmStatic
        fun getEndpointUrl(request: HttpServletRequest): String {
            val pathWithinApplication = UrlPathHelper().getPathWithinApplication(request)
            return pathWithinApplication.removeSuffix("/")
        }

        /**
         * Checks whether provided request ends with slash.
         */
        @JvmStatic
        fun isPathEndsWithSlash(request: HttpServletRequest): Boolean {
            var url = request.servletPath
            if (request.pathInfo != null) {
                url += request.pathInfo
            }
            return url.endsWith("/")
        }
    }
}