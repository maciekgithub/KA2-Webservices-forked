package pl.euler.bgs.restapi.web.api

data class DatabaseRequest(val requestUrl: String, val requestParams: String, val headers: ApiHeaders, val requestJson: String) {
    override fun toString(): String {
        return "DatabaseResponse(requestUrl=$requestUrl, requestParams=$requestParams, headers=$headers, json='$requestJson')"
    }
}