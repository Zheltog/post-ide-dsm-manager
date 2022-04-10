package su.nsk.iae.post.dsm.manager.infrastructure.api

data class ApiEntry(
    val method: ApiMethod,
    val url: String,
    val requestBody: String,
    val responseBody: String
)