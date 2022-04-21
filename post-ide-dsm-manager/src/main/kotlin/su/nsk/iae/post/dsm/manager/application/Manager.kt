package su.nsk.iae.post.dsm.manager.application

import com.fasterxml.jackson.databind.ObjectMapper
import su.nsk.iae.post.dsm.manager.domain.AvailableModules
import su.nsk.iae.post.dsm.manager.domain.Module
import java.io.*
import java.net.InetAddress
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets.UTF_8
import java.util.*
import kotlin.Result.Companion.failure

object Manager {

    private lateinit var availableModules: AvailableModules
    private val aliveModules: MutableList<Module> = mutableListOf()

    fun registerModule(name: String, host: String, port: Int) {
        val module = Module(name = name, host = host, port = port)
        aliveModules.add(module)
        logInfo("registered new module: name = $name, host = $host, port = $port")
    }

    fun getModules(isModuleAlive: (Module) -> Boolean): List<Module> {
        logInfo("updating alive modules list")
        removeDeadModules(isModuleAlive)
        return aliveModules
    }

    fun runModule(
        moduleName: String,
        request: String,
        isModuleAlive: (Module) -> Boolean,
        runModule: (Module, String) -> Result<String>
    ): Result<String> {
        logInfo("running module $moduleName")

        val module = aliveModules.firstOrNull { it.name == moduleName }
            ?: return failure(Exception("module not found"))

        if (!isModuleAlive(module)) {
            aliveModules.remove(module)
            return failure(Exception("module disconnected"))
        }

        return runModule(module, request)
    }

    fun readAvailableModules(availableModulesJson: String?) {
        logInfo("getting available modules")
        val mapper = ObjectMapper()
        val input =
            if (availableModulesJson == null)
                Manager.javaClass.getResourceAsStream("/available-modules.json")
            else
                FileInputStream(File(availableModulesJson))
        if (input == null) {
            logError("could not get available modules")
            return
        }
        availableModules = mapper.readValue(input, AvailableModules::class.java)
        logInfo("available modules: $availableModules")
    }

    fun startAvailableModules() {
        logInfo("starting available modules")
        val mHost = InetAddress.getLocalHost().hostAddress
        val properties = Properties()
        properties.load(Manager.javaClass.classLoader
            .getResourceAsStream("application.properties"))
        val mPort = properties.getProperty("server.port")
        val managerAddress = "http://$mHost:$mPort"
        val dir = availableModules.directory ?: ""
        availableModules.modulesJarNames?.forEach {
            logInfo("starting $dir$it.jar with -ma $managerAddress")
            val process = Runtime.getRuntime().exec(
                "java -jar $dir$it.jar -ma $managerAddress"
            )
            val errorIS: InputStream = process.errorStream
            val errorText = StringBuilder()
            val reader = BufferedReader(InputStreamReader(
                errorIS, Charset.forName(UTF_8.name())
            ))
            var c = 0
            while (c != -1) {
                c = reader.read()
                errorText.append(c.toChar())
            }
            logError(errorText.toString())
        }
    }

    private fun removeDeadModules(isModuleAlive: (Module) -> Boolean) {
        logInfo("removing dead modules from alive modules list")
        aliveModules.removeAll(aliveModules.filter {
            !isModuleAlive(it)
        })
    }

    private fun logInfo(message: String) = Logger.info(
        Manager.javaClass, message
    )

    private fun logError(message: String) = Logger.error(
        Manager.javaClass, message
    )
}