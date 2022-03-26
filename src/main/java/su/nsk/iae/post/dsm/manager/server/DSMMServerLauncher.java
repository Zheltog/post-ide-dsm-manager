package su.nsk.iae.post.dsm.manager.server;

import su.nsk.iae.post.dsm.manager.common.DSMMLogger;
import su.nsk.iae.post.dsm.manager.common.SocketLauncher;
import su.nsk.iae.post.dsm.manager.client.DSMMClient;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DSMMServerLauncher {

    public void start(int port) {
        DSMMLogger.info(DSMMServerLauncher.class, "starting...");

        DSMMServerImpl server = new DSMMServerImpl();
        ExecutorService threadPool = Executors.newCachedThreadPool();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            DSMMLogger.info(
                    DSMMServerLauncher.class,
                    "created server socket on " +
                            serverSocket.getInetAddress().getHostName() +
                            ":" +
                            serverSocket.getLocalPort()
            );
            threadPool.submit(() -> {
                while (true) {
                    Socket socket = serverSocket.accept();
                    DSMMLogger.info(DSMMServerLauncher.class, "accepted connection...");
                    SocketLauncher<DSMMClient> launcher = new SocketLauncher<>(
                            server, DSMMClient.class, socket
                    );
                    Runnable removeClient = server.addClient(launcher.getRemoteProxy());
                    launcher.startListening().thenRun(removeClient);
                }
            });
        } catch (IOException e) {
            DSMMLogger.error(DSMMServerLauncher.class, e.getMessage());
        }
    }
}