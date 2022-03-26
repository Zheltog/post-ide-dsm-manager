package su.nsk.iae.post.dsm.manager.client;

import org.eclipse.lsp4j.jsonrpc.Launcher;
import su.nsk.iae.post.dsm.manager.common.DSMMLogger;
import su.nsk.iae.post.dsm.manager.common.ServerUtils;
import su.nsk.iae.post.dsm.manager.server.DSMMServer;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import static java.nio.channels.Channels.newInputStream;
import static java.nio.channels.Channels.newOutputStream;
import static org.eclipse.lsp4j.jsonrpc.Launcher.createLauncher;

public class DSMMClientLauncher {

    private AsynchronousSocketChannel socketChannel;

    private final DSMMClientImpl client;

    public DSMMClientLauncher(DSMMClientImpl client) {
        this.client = client;
    }

    public void start(String serverHost, int serverPort) {
        start(serverHost, serverPort, "localhost", ServerUtils.findFreePort());
    }

    public void start(String serverHost, int serverPort, String clientHost, int clientPort) {
        DSMMLogger.info(DSMMClientLauncher.class, "starting...");

        try {
            this.socketChannel = AsynchronousSocketChannel
                    .open()
                    .bind(new InetSocketAddress(clientHost, clientPort));
            DSMMLogger.info(
                    DSMMClientLauncher.class,
                    "successfully created socket channel for " + clientHost + ":" + clientPort
            );
            socketChannel.connect(new InetSocketAddress(serverHost, serverPort)).get();
            DSMMLogger.info(
                    DSMMClientLauncher.class,
                    "successfully connected to server on " + serverHost + ":" + serverPort
            );
            final Launcher<DSMMServer> launcher = createLauncher(
                    client, DSMMServer.class,
                    newInputStream(socketChannel),
                    newOutputStream(socketChannel)
            );
            final DSMMServer server = launcher.getRemoteProxy();
            DSMMLogger.info(DSMMClientLauncher.class, "successfully got server proxy");
            client.start(server);
        } catch (Exception e) {
            DSMMLogger.error(DSMMClientLauncher.class, e.getMessage());
        }
    }

    public void stop() {
        DSMMLogger.info(DSMMClientLauncher.class, "stopping...");
        try {
            if (socketChannel != null && socketChannel.isOpen()) {
                socketChannel.close();
                DSMMLogger.info(
                        DSMMClientLauncher.class,
                        "socket channel closed successfully"
                );
            } else {
                DSMMLogger.info(
                        DSMMClientLauncher.class,
                        "socket channel was not open"
                );
            }
            if (client != null) {
                client.removeFromServer();
                DSMMLogger.info(
                        DSMMClientLauncher.class,
                        "removed client from server's clients list"
                );
            }
        } catch (Exception e) {
            DSMMLogger.error(DSMMClientLauncher.class, e.getMessage());
        }
    }
}