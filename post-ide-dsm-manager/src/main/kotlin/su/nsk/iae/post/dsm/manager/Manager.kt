package su.nsk.iae.post.dsm.manager

import su.nsk.iae.post.dsm.manager.common.Logger
import su.nsk.iae.post.dsm.manager.common.ServerUtils

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
        // TODO: для каждого модуля нужно проверить, жив ли он
        return modules
    }
}