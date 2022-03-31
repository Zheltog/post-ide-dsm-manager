package su.nsk.iae.post.dsm.manager

import su.nsk.iae.post.dsm.manager.common.Logger
import su.nsk.iae.post.dsm.manager.common.ServerUtils
import java.net.ProxySelector
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers

object Manager {

    private val modules: MutableList<Module> = mutableListOf()

    fun registerModule(name: String): Int {
        val freePort = ServerUtils.findFreePort()
        val module = Module(name = name, port = freePort)
        modules.add(module)
        Logger.info(Manager.javaClass, "registered new module: name = $name, port = $freePort")
        return freePort
    }

    fun getModules(): List<Module> {
        checkModules()
        return modules
    }

    private fun checkModules() {
        val modulesToRemove = mutableListOf<Module>()
        for (module in modules) {
            val request = HttpRequest.newBuilder()
                .uri(URI("http://127.0.0.1:${module.port}"))
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