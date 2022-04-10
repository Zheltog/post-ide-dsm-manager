package su.nsk.iae.post.dsm.manager.infrastructure.requests

data class NewModuleRequestBody(
    val name: String? = null,
    val port: Int? = null
)