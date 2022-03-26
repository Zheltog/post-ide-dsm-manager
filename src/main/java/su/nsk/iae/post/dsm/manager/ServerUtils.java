package su.nsk.iae.post.dsm.manager;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerUtils {

    public static int findFreePort() {
        try (ServerSocket socket = new ServerSocket(0)) {
            socket.setReuseAddress(true);
            return socket.getLocalPort();
        } catch (IOException e) {
            DSMManagerLogger.error(ServerUtils.class, e.getMessage());
        }
        throw new IllegalStateException("Could not find a free TCP/IP port");
    }
}