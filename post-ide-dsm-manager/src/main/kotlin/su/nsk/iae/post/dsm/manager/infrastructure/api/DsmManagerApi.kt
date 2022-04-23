package su.nsk.iae.post.dsm.manager.infrastructure.api

import su.nsk.iae.post.dsm.manager.infrastructure.api.ApiMethod.GET
import su.nsk.iae.post.dsm.manager.infrastructure.api.ApiMethod.POST

object DsmManagerApi {

    private val api = listOf(
        ApiEntry(GET, "/alive-modules", "empty", "alive modules list"),
        ApiEntry(GET, "/available-modules", "empty", "available modules list"),
        ApiEntry(POST, "/new-module", "module description", "empty"),
        ApiEntry(GET, "/start-all", "empty", "result"),
        ApiEntry(GET, "/start/{moduleName}", "empty", "result"),
        ApiEntry(GET, "/stop/{moduleName}", "empty", "result"),
        ApiEntry(POST, "/run/{moduleName}", "request for module", "result")
    )

    fun asHtml(): String {
        val stringBuilder = StringBuilder()
        api.forEach {
            stringBuilder.append(
                "${it.method} ${it.url}<br>" +
                "Request body: ${it.requestBody}<br>" +
                "Response body: ${it.responseBody}<br><br>"
            )
        }
        return stringBuilder.toString()
    }
}