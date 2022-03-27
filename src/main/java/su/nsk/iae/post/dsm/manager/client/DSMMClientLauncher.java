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

    /**
     * start launcher on localhost:any-available-port
     * @param serverHost server hostname
     * @param serverPort server port
     */
    public void start(String serverHost, int serverPort) {
        start(serverHost, serverPort, "localhost", ServerUtils.findFreePort());
    }

    /**
     * start launcher on exact host:port
     * @param serverHost server hostname
     * @param serverPort server port
     * @param clientHost client hostname
     * @param clientPort client port
     */
    public void start(String serverHost, int serverPort, String clientHost, int clientPort) {
        logInfo("starting...");
        try {
            this.socketChannel = AsynchronousSocketChannel
                    .open().bind(new InetSocketAddress(clientHost, clientPort));
            logInfo("successfully created socket channel for " + clientHost + ":" + clientPort);
            socketChannel.connect(new InetSocketAddress(serverHost, serverPort)).get();
            logInfo("successfully connected to server on " + serverHost + ":" + serverPort);
            final Launcher<DSMMServer> launcher = createLauncher(
                    client, DSMMServer.class, newInputStream(socketChannel), newOutputStream(socketChannel)
            );
            final DSMMServer server = launcher.getRemoteProxy();
            logInfo("successfully got server proxy");
            client.start();
        } catch (Exception e) {
            logError(e.getMessage());
            stop();
        }
    }

    public void stop() {
        logInfo("stopping...");
        try {
            if (socketChannel != null && socketChannel.isOpen()) {
                socketChannel.close();
                logInfo("socket channel closed successfully");
            } else {
                logInfo("socket channel was not open");
            }
        } catch (Exception e) {
            logError(e.getMessage());
        }
    }

    private void logInfo(String message) {
        DSMMLogger.info(DSMMClientLauncher.class, message);
    }

    private void logError(String message) {
        DSMMLogger.error(DSMMClientLauncher.class, message);
    }
}