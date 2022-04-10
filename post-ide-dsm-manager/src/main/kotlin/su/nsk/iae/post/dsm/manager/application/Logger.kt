package su.nsk.iae.post.dsm.manager.application

import org.apache.log4j.Logger.*

object Logger {

    private val log = getLogger(Logger::class.java)

    fun info(c: Class<*>, message: String) {
        println(c.simpleName + ": " + message)
        log.info(c.simpleName + ": " + message)
    }

    fun error(c: Class<*>, message: String) {
        println(c.simpleName + ": " + message)
        log.error(c.simpleName + ": " + message)
    }
}