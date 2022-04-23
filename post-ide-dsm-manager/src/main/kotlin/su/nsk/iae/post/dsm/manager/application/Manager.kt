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
import kotlin.Result.Companion.success

object Manager {

    private val managerAddress = "http://${InetAddress.getLocalHost().hostAddress}:${getManagerPort()}"

    private var availableModules: AvailableModules? = null
    private val aliveModules: MutableList<Module> = mutableListOf()

    // new-module
    fun registerModule(name: String, host: String, port: Int) {
        val module = Module(name = name, host = host, port = port)
        aliveModules.add(module)
        logInfo("registered new module: name = $name, host = $host, port = $port")
    }

    // alive-modules
    fun getAliveModules(isModuleAlive: (Module) -> Boolean): List<Module> {
        logInfo("updating alive modules list")
        removeDeadModules(isModuleAlive)
        return aliveModules
    }

    // available-modules
    fun getAvailableModules() =
        if (availableModules == null) {
            readAvailableModules(null)!!
        } else availableModules!!

    // run
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

    // start-all
    fun startAvailableModules(availableModulesJson: String?): Result<String> {
        logInfo("starting available modules")
        availableModules ?: readAvailableModules(availableModulesJson)
        val am = availableModules
            ?: return failure(Exception("could not load available modules json file"))
        am.modulesJarNames?.forEach { name ->
            startModule(name, availableModulesJson).onFailure { error ->
                return failure(error)
            }
        }
        return success("available modules started successfully")
    }

    // start
    fun startModule(moduleName: String, availableModulesJson: String?): Result<String> {
        logInfo("starting module: $moduleName")
        availableModules ?: readAvailableModules(availableModulesJson)
        val am = availableModules
            ?: return failure(Exception("could not load available modules json file"))
        val dir = am.directory ?: ""
        if (am.modulesJarNames?.contains(moduleName) != true) {
            return failure(Exception("module $moduleName is not available"))
        }
        logInfo("starting $dir$moduleName.jar with -ma $managerAddress")
        val process = Runtime.getRuntime().exec(
            "java -jar $dir$moduleName.jar -ma $managerAddress"
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
        val errorString = errorText.toString()
        if (errorString.isNotEmpty()) {
            logError(errorString)
            return failure(Exception(errorString))
        }
        return success("module $moduleName started successfully")
    }

    // stop
    fun stopModule(
        moduleName: String,
        stopModule: (Module) -> Result<String>
    ): Result<String> {
        val module = aliveModules.firstOrNull { it.name == moduleName }
            ?: return failure(Exception("module not found"))

        aliveModules.remove(module)

        return stopModule(module)
    }

    private fun readAvailableModules(
        availableModulesJson: String?
    ): AvailableModules? {
        logInfo("getting available modules")
        val mapper = ObjectMapper()
        val input =
            if (availableModulesJson == null)
                Manager.javaClass.getResourceAsStream("/available-modules.json")
            else
                FileInputStream(File(availableModulesJson))
        if (input == null) {
            logError("could not load available modules json file")
            return null
        }
        availableModules = mapper.readValue(input, AvailableModules::class.java)
        logInfo("available modules: $availableModules")
        return availableModules
    }

    private fun removeDeadModules(isModuleAlive: (Module) -> Boolean) {
        logInfo("removing dead modules from alive modules list")
        aliveModules.removeAll(aliveModules.filter {
            !isModuleAlive(it)
        })
    }

    private fun getManagerPort(): Int {
        val properties = Properties()
        properties.load(Manager.javaClass.classLoader
            .getResourceAsStream("application.properties"))
        return properties.getProperty("server.port").toInt()
    }

    private fun logInfo(message: String) = Logger.info(
        Manager.javaClass, message
    )

    private fun logError(message: String) = Logger.error(
        Manager.javaClass, message
    )
}