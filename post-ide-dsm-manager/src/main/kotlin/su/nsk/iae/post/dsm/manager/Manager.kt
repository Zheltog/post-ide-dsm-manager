package su.nsk.iae.post.dsm.manager

import su.nsk.iae.post.dsm.manager.common.Logger
import su.nsk.iae.post.dsm.manager.requests.DsmRequestBody
import java.net.ProxySelector
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse
import java.net.http.HttpResponse.BodyHandlers

object Manager {

    private val modules: MutableList<Module> = mutableListOf()

    fun registerModule(name: String, host: String, port: Int) {
        val module = Module(name = name, host = host, port = port)
        modules.add(module)
        Logger.info(
            Manager.javaClass,
            "registered new module: name = $name, host = $host, port = $port"
        )
    }

    fun getModules(): List<Module> {
        checkModules()
        return modules
    }

    fun runModule(moduleName: String, requestBody: DsmRequestBody): String {
        val requestBodyJsonStr = requestBody.toJsonString()

        Logger.info(
            Manager.javaClass,
            "running module $moduleName with request-body $requestBodyJsonStr"
        )

        val module = modules.firstOrNull { it.name == moduleName }
            ?: return "Module not found"

        val request = HttpRequest.newBuilder()
            .uri(URI("http://${module.host}:${module.port}/run"))
            .headers("Content-Type", "application/json")
            .POST(BodyPublishers.ofString(requestBody.toJsonString()))
            .build()
        return try {
            val response: HttpResponse<String> = HttpClient.newBuilder()
                .proxy(ProxySelector.getDefault())
                .build()
                .send(request, BodyHandlers.ofString())
            response.body()
        } catch (e: Exception) {
            "Error occurred: ${e.message}"
        }
    }

    private fun checkModules() {
        val modulesToRemove = mutableListOf<Module>()
        for (module in modules) {
            val request = HttpRequest.newBuilder()
                .uri(URI("http://${module.host}:${module.port}"))
                .GET()
                .build()
            try {
                HttpClient.newBuilder()
                    .proxy(ProxySelector.getDefault())
                    .build()
                    .send(request, BodyHandlers.ofString())
            } catch (e: Exception) {
                modulesToRemove.add(module)
            }
        }
        modules.removeAll(modulesToRemove)
    }
}