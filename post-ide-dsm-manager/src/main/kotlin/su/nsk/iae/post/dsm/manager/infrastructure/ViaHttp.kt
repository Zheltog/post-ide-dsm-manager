package su.nsk.iae.post.dsm.manager.infrastructure

import su.nsk.iae.post.dsm.manager.application.Logger
import su.nsk.iae.post.dsm.manager.application.Manager
import su.nsk.iae.post.dsm.manager.domain.Module
import su.nsk.iae.post.dsm.manager.infrastructure.requests.DsmRequestBody
import java.net.ProxySelector
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success

object ViaHttp {

    val isModuleAlive: (Module) -> Boolean = { module ->
        val request = HttpRequest.newBuilder()
            .uri(URI("http://${module.host}:${module.port}"))
            .GET()
            .build()
        try {
            HttpClient.newBuilder()
                .proxy(ProxySelector.getDefault())
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString())
            true
        } catch (e: Exception) {
            false
        }
    }

    val runModule: (Module, DsmRequestBody) -> Result<String> = { module, requestBody ->
        val requestBodyJsonStr = requestBody.toJsonString()

        Logger.info(
            Manager.javaClass,
            "running module ${module.name} with request-body $requestBodyJsonStr"
        )

        val request = HttpRequest.newBuilder()
            .uri(URI("http://${module.host}:${module.port}/run"))
            .headers("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody.toJsonString()))
            .build()

        try {
            val response: HttpResponse<String> = HttpClient.newBuilder()
                .proxy(ProxySelector.getDefault())
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString())

            success(response.body())
        } catch (e: Exception) {
            failure(e)
        }
    }
}