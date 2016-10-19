package pl.euler.bgs.restapi.web.api

//todo add builder for this class

data class DatabaseRequest(val requestUrl: String, val requestParams: String,
                           val headerAgent: String, val headerDate: String, val requestJson: String) {
    override fun toString(): String {
        return "DatabaseResponse(requestUrl=$requestUrl, requestParams=$requestParams, headerAgent=$headerAgent, " +
                "headerDate=$headerDate, json='$requestJson')"
    }
}