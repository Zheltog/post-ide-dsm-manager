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
    for (arg in args) {
        if ("-help" == arg) {
            help()
            return
        }
    }
    Manager.readAvailableModules()
    runApplication<App>(*args)
    Manager.startAvailableModules()
}

private fun help() {
    Logger.info(App::class.java, "available running configurations:")
    Logger.info(App::class.java, "java -Dserver.port=<port-number> -jar <jar-name>")
}