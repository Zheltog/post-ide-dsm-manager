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
import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.concurrent.Executors.newSingleThreadExecutor;

public class DSMMServerLauncher {

    private boolean shouldRun = true;
    private AsynchronousServerSocketChannel serverSocketChannel;
    private final DSMMServerImpl server;

    public DSMMServerLauncher(DSMMServerImpl server) {
        this.server = server;
    }

    public void start(String host, int port) {
        logInfo("starting on " + host + ":" + port + "...");
        try {
            this.serverSocketChannel = AsynchronousServerSocketChannel
                    .open().bind(new InetSocketAddress(host, port));
            logInfo("successfully created server socket channel for " + host + ":" + port);
            while (shouldRun) {
                logInfo("waiting for connection...");
                AsynchronousSocketChannel socketChannel = serverSocketChannel.accept().get();
                logInfo("accepted new connection");
                final Launcher<DSMMClient> launcher = Launcher.createLauncher(
                        server, DSMMClient.class, newInputStream(socketChannel), newOutputStream(socketChannel)
                );
                final DSMMClient client = launcher.getRemoteProxy();
                logInfo("successfully got client proxy");
                Runnable removeClient = server.addClient(client);
                launch(launcher, removeClient);
            }
        } catch (Exception e) {
            logError(e.getMessage());
            stop();
        }
    }

    private void launch(Launcher<DSMMClient> launcher, Runnable finishAction) {
        runAsync(() -> {
                    try {
                        launcher.startListening().get();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                },
                newSingleThreadExecutor()
        ).thenRun(finishAction);
    }

    public void stop() {
        logInfo("stopping...");
        shouldRun = false;
        try {
            if (serverSocketChannel != null && serverSocketChannel.isOpen()) {
                serverSocketChannel.close();
                logInfo("server socket channel closed successfully");
            } else {
                logInfo("server socket channel was not open");
            }
        } catch (IOException e) {
            logError(e.getMessage());
        }
    }

    private void logInfo(String message) {
        DSMMLogger.info(DSMMServerLauncher.class, message);
    }

    private void logError(String message) {
        DSMMLogger.error(DSMMServerLauncher.class, message);
    }
}