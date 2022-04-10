package su.nsk.iae.post.dsm.manager.infrastructure.responses

data class ResponseBody(
    val code: ResponseCode,
    val content: Any?
)