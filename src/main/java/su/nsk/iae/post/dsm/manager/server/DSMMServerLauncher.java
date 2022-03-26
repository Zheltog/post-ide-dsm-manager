package su.nsk.iae.post.dsm.manager.server;

import org.eclipse.lsp4j.jsonrpc.Launcher;
import su.nsk.iae.post.dsm.manager.common.DSMMLogger;
import su.nsk.iae.post.dsm.manager.client.DSMMClient;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.CompletableFuture;
import static java.nio.channels.Channels.newInputStream;
import static java.nio.channels.Channels.newOutputStream;
import static java.util.concurrent.Executors.newCachedThreadPool;

public class DSMMServerLauncher {

    public void start(String host, int port) {
        DSMMLogger.info(DSMMServerLauncher.class, "starting...");

        try (AsynchronousServerSocketChannel serverSocket = AsynchronousServerSocketChannel
                .open()
                .bind(new InetSocketAddress(host, port))
        ) {
            DSMMLogger.info(
                    DSMMServerLauncher.class,
                    "successfully created server socket channel for " + host + ":" + port
            );
            DSMMServerImpl server = new DSMMServerImpl();
            while (true) {
                DSMMLogger.info(DSMMServerLauncher.class, "waiting for connection...");
                AsynchronousSocketChannel socketChannel = serverSocket.accept().get();
                DSMMLogger.info(DSMMServerLauncher.class, "accepted new connection");
                final Launcher<DSMMClient> launcher = Launcher.createIoLauncher(
                        server, DSMMClient.class,
                        newInputStream(socketChannel),
                        newOutputStream(socketChannel),
                        newCachedThreadPool(), msg -> msg
                );
                final DSMMClient client = launcher.getRemoteProxy();
                DSMMLogger.info(DSMMServerLauncher.class, "successfully got client proxy");
                Runnable removeClient = server.addClient(client);
                CompletableFuture.supplyAsync(launcher::startListening).thenRun(removeClient);
            }
        } catch (Exception e) {
            DSMMLogger.error(DSMMServerLauncher.class, e.getMessage());
        }
    }
}