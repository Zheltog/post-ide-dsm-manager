package su.nsk.iae.post.dsm.manager.client;

import org.eclipse.lsp4j.jsonrpc.Launcher;
import su.nsk.iae.post.dsm.manager.common.DSMMLogger;
import su.nsk.iae.post.dsm.manager.server.DSMMServer;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import static java.nio.channels.Channels.newInputStream;
import static java.nio.channels.Channels.newOutputStream;
import static java.util.concurrent.Executors.newCachedThreadPool;

public class DSMMClientLauncher {

    private final DSMMClient client;

    public DSMMClientLauncher(DSMMClient client) {
        this.client = client;
    }

    public void start(String serverHost, int serverPort, String clientHost, int clientPort) {
        DSMMLogger.info(DSMMClientLauncher.class, "starting...");

        try (AsynchronousSocketChannel socketChannel = AsynchronousSocketChannel
                .open()
                .bind(new InetSocketAddress(clientHost, clientPort))
        ) {
            DSMMLogger.info(
                    DSMMClientLauncher.class,
                    "successfully created socket channel for " + clientHost + ":" + clientPort
            );
            socketChannel.connect(new InetSocketAddress(serverHost, serverPort)).get();
            DSMMLogger.info(
                    DSMMClientLauncher.class,
                    "successfully connected to server on " + serverHost + ":" + serverPort
            );
            final Launcher<DSMMServer> launcher = Launcher.createIoLauncher(
                    client, DSMMServer.class,
                    newInputStream(socketChannel),
                    newOutputStream(socketChannel),
                    newCachedThreadPool(), msg -> msg
            );
            final DSMMServer server = launcher.getRemoteProxy();
            DSMMLogger.info(DSMMClientLauncher.class, "successfully got server proxy");
            client.start(server);
        } catch (Exception e) {
            DSMMLogger.error(DSMMClientLauncher.class, e.getMessage());
        }
    }
}