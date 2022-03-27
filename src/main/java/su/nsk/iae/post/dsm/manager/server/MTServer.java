package su.nsk.iae.post.dsm.manager.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MTServer {

    private ServerSocket serverSocket;
    private int port;

    public MTServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Starting the socket server at port:" + port);

        while (true) {
            System.out.println("Waiting for clients...");
            Socket client = serverSocket.accept();
            System.out.println("The following client has connected: "
                    + client.getInetAddress().getCanonicalHostName());
            Thread thread = new Thread(new ClientHandler(client));
            thread.start();
        }
    }
}