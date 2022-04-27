package su.nsk.iae.post.dsm.manager.infrastructure

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
            e.printStackTrace()
        }
        throw IllegalStateException("Could not find a free TCP/IP port")
    }
}