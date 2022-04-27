package su.nsk.iae.post.dsm.manager

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import su.nsk.iae.post.dsm.manager.application.Logger
import su.nsk.iae.post.dsm.manager.application.Manager
import su.nsk.iae.post.dsm.manager.infrastructure.ServerUtils
import java.lang.System.setProperty

@SpringBootApplication
open class App

fun main(args: Array<String>) {
    Logger.info(
        App::class.java,
        "use -help to see available running configurations"
    )
    var availableModulesJson: String? = null
    var startAvailableModules = false
    var port: String? = null
    for (i in args.indices) {
        if ("-help" == args[i]) {
            help()
            return
        }
        if ("-amj" == args[i]) {
            availableModulesJson = args[i+1]
        }
        if ("-sam" == args[i]) {
            startAvailableModules = true
        }
        if ("-p" == args[i]) {
            port = args[i+1]
        }
        if ("-ap" == args[i]) {
            port = ServerUtils.findFreePort().toString()
        }
    }
    if (port != null) {
        setProperty("server.port", port)
        Manager.managerPort = port.toInt()
        Logger.info(App::class.java, "running manager on port $port")
    }
    runApplication<App>(*args)
    if (startAvailableModules) {
        Manager.startAvailableModules(availableModulesJson)
    }
}

private fun help() {
    Logger.info(App::class.java, "available running configurations:")
    Logger.info(App::class.java, "java -Dserver.port=<port-number> -jar <jar name>")
    Logger.info(App::class.java, "java -jar <jar name> -amj <available-modules.json filepath> (taking from resource folder by default)")
    Logger.info(App::class.java, "java -jar <jar name> -sam (start available modules)")
    Logger.info(App::class.java, "java -jar <jar name> -p <port> (define exact port)")
    Logger.info(App::class.java, "java -jar <jar name> -ap (auto-port == any free one)")
}