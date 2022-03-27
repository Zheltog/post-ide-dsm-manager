package su.nsk.iae.post.dsm.manager.server;

import su.nsk.iae.post.dsm.manager.common.Logger;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private final int port;

    private boolean shouldRun = true;
    private ServerSocket serverSocket;

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            logInfo("starting server at port: " + port + "...");

            while (shouldRun) {
                logInfo("waiting for connection...");
                Socket client = serverSocket.accept();
                logInfo("accepted connection from: " + client.getInetAddress().getCanonicalHostName());
                new Thread(new ClientHandler(client)).start();
            }
        } catch (IOException e) {
            logError(e.getMessage());
        }
    }

    public void stop() {
        try {
            logInfo("stopping...");
            shouldRun = false;
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                logInfo("server socket closed successfully");
            } else {
                logInfo("server socket was not open");
            }
        } catch (IOException e) {
            logError(e.getMessage());
        }
    }

    private void logInfo(String message) {
        Logger.info(Server.class, message);
    }

    private void logError(String message) {
        Logger.error(Server.class, message);
    }
}