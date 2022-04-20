package su.nsk.iae.post.dsm.manager

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import su.nsk.iae.post.dsm.manager.application.Logger
import su.nsk.iae.post.dsm.manager.application.Manager

@SpringBootApplication
open class App

fun main(args: Array<String>) {
    Logger.info(
        App::class.java,
        "use -help to see available running configurations"
    )
    var availableModulesJson: String? = null
    var startAvailableModules = true
    for (i in args.indices) {
        if ("-help" == args[i]) {
            help()
            return
        }
        if ("-am" == args[i]) {
            availableModulesJson = args[i+1]
        }
        if ("-dsam" == args[i]) {
            startAvailableModules = false
        }
    }
    runApplication<App>(*args)
    if (startAvailableModules) {
        Manager.readAvailableModules(availableModulesJson)
        Manager.startAvailableModules()
    }
}

private fun help() {
    Logger.info(App::class.java, "available running configurations:")
    Logger.info(App::class.java, "java -Dserver.port=<port-number> -jar <jar name>")
    Logger.info(App::class.java, "java -jar <jar name> -am <available-modules.json filepath> (taking from resource folder by default)")
    Logger.info(App::class.java, "java -jar <jar name> -dsam (don't start available modules)")
}