package su.nsk.iae.post.dsm.manager.server;

import org.eclipse.lsp4j.jsonrpc.Launcher;
import su.nsk.iae.post.dsm.manager.common.DSMMLogger;
import su.nsk.iae.post.dsm.manager.client.DSMMClient;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import static java.nio.channels.Channels.newInputStream;
import static java.nio.channels.Channels.newOutputStream;
import static java.util.concurrent.CompletableFuture.supplyAsync;

public class DSMMServerLauncher {

    private AsynchronousServerSocketChannel serverSocketChannel;
    private boolean shouldRun = true;

    private final DSMMServer server;

    public DSMMServerLauncher(DSMMServer server) {
        this.server = server;
    }

    public void start(String host, int port) {
        DSMMLogger.info(
                DSMMServerLauncher.class,
                "starting on " + host + ":" + port + "..."
        );

        try {
            this.serverSocketChannel = AsynchronousServerSocketChannel
                    .open().bind(new InetSocketAddress(host, port));
            DSMMLogger.info(
                    DSMMServerLauncher.class,
                    "successfully created server socket channel for " + host + ":" + port
            );
            while (shouldRun) {
                DSMMLogger.info(DSMMServerLauncher.class, "waiting for connection...");
                AsynchronousSocketChannel socketChannel = serverSocketChannel.accept().get();
                DSMMLogger.info(DSMMServerLauncher.class, "accepted new connection");
                final Launcher<DSMMClient> launcher = Launcher.createLauncher(
                        server, DSMMClient.class,
                        newInputStream(socketChannel),
                        newOutputStream(socketChannel)
                );
                final DSMMClient client = launcher.getRemoteProxy();
                DSMMLogger.info(DSMMServerLauncher.class, "successfully got client proxy");
                supplyAsync(launcher::startListening)
                        .thenRun(() -> server.addClient(client));
            }
        } catch (Exception e) {
            DSMMLogger.error(DSMMServerLauncher.class, e.getMessage());
            stop();
        }
    }

    public void stop() {
        DSMMLogger.info(DSMMServerLauncher.class, "stopping...");
        shouldRun = false;
        try {
            if (serverSocketChannel != null && serverSocketChannel.isOpen()) {
                serverSocketChannel.close();
                DSMMLogger.info(
                        DSMMServerLauncher.class,
                        "server socket channel closed successfully"
                );
            } else {
                DSMMLogger.info(
                        DSMMServerLauncher.class,
                        "server socket channel was not open"
                );
            }
        } catch (IOException e) {
            DSMMLogger.error(DSMMServerLauncher.class, e.getMessage());
        }
    }
}