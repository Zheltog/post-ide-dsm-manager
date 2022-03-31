package su.nsk.iae.post.dsm.manager.common

import su.nsk.iae.post.dsm.manager.common.Logger.error
import java.io.IOException
import java.net.ServerSocket

object ServerUtils {

    fun findFreePort(): Int {
        try {
            ServerSocket(0).use { socket ->
                socket.reuseAddress = true
                return socket.localPort
            }
        } catch (e: IOException) {
            error(ServerUtils::class.java, e.message!!)
        }
        throw IllegalStateException("Could not find a free TCP/IP port")
    }
}