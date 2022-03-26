package su.nsk.iae.post.dsm.manager.client;

import su.nsk.iae.post.dsm.manager.common.DSMMLogger;
import su.nsk.iae.post.dsm.manager.common.SocketLauncher;
import su.nsk.iae.post.dsm.manager.server.DSMMServer;
import java.io.IOException;
import java.net.Socket;

public class DSMMClientLauncher {

    private final DSMMClient client;

    public DSMMClientLauncher(DSMMClient client) {
        this.client = client;
    }

    public void start(String host, int port) {
        DSMMLogger.info(DSMMClientLauncher.class, "starting...");

        try (Socket socket = new Socket(host, port)) {
            SocketLauncher<DSMMServer> launcher = new SocketLauncher<>(
                    client, DSMMServer.class, socket
            );
            launcher.startListening().thenRun(() -> System.exit(0));
            client.start(launcher.getRemoteProxy());
        } catch (IOException e) {
            DSMMLogger.error(DSMMClientLauncher.class, e.getMessage());
        }
    }
}