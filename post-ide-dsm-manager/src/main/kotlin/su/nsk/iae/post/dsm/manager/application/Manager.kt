package su.nsk.iae.post.dsm.manager.application

import su.nsk.iae.post.dsm.manager.domain.Module
import kotlin.Result.Companion.failure

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

    fun getModules(isModuleAlive: (Module) -> Boolean): List<Module> {
        removeDeadModules(isModuleAlive)
        return modules
    }

    fun runModule(
        moduleName: String,
        request: String,
        isModuleAlive: (Module) -> Boolean,
        runModule: (Module, String) -> Result<String>
    ): Result<String> {
        val module = modules.firstOrNull { it.name == moduleName }
            ?: return failure(Exception("Module not found"))

        if (!isModuleAlive(module)) {
            modules.remove(module)
            return failure(Exception("Module disconnected"))
        }

        return runModule(module, request)
    }

    private fun removeDeadModules(isModuleAlive: (Module) -> Boolean) {
        val modulesToRemove = mutableListOf<Module>()
        for (module in modules) {
            if (!isModuleAlive(module)) {
                modulesToRemove.add(module)
            }
        }
        modules.removeAll(modulesToRemove)
    }
}