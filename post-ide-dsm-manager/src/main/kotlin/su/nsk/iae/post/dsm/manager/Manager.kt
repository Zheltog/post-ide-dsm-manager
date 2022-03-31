package su.nsk.iae.post.dsm.manager

import su.nsk.iae.post.dsm.manager.common.Logger
import java.net.ProxySelector
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
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