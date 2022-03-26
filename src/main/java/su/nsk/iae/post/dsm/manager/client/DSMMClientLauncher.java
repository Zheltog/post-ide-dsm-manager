package su.nsk.iae.post.dsm.manager.client;

import su.nsk.iae.post.dsm.manager.common.SocketLauncher;
import su.nsk.iae.post.dsm.manager.server.DSMMServer;
import java.net.Socket;

public class DSMMClientLauncher {

    public void start(String host, int port) {
        DSMMClientImpl client = new DSMMClientImpl();

        try (Socket socket = new Socket(host, port)) {
            SocketLauncher<DSMMServer> launcher = new SocketLauncher<>(
                    client, DSMMServer.class, socket
            );
            launcher.startListening().thenRun(() -> System.exit(0));
            client.start(launcher.getRemoteProxy());
        } catch (Exception e) {}
    }
}