package su.nsk.iae.post.dsm.manager.server;

import su.nsk.iae.post.dsm.manager.common.SocketLauncher;
import su.nsk.iae.post.dsm.manager.client.DSMMClient;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DSMMServerLauncher {

    public void start(int port) {
        DSMMServerImpl server = new DSMMServerImpl();
        ExecutorService threadPool = Executors.newCachedThreadPool();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            threadPool.submit(() -> {
                while (true) {
                    Socket socket = serverSocket.accept();
                    SocketLauncher<DSMMClient> launcher = new SocketLauncher<>(
                            server, DSMMClient.class, socket
                    );
                    Runnable removeClient = server.addClient(launcher.getRemoteProxy());
                    launcher.startListening().thenRun(removeClient);
                }
            });
            System.in.read();
            System.exit(0);
        }
        catch (Exception e) {}
    }
}