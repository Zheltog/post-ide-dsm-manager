package su.nsk.iae.post.dsm.manager.server;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientHandler implements Runnable {

    private Socket client;

    public ClientHandler(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            System.out.println("Thread started with name: " + Thread.currentThread().getName());
            readResponse();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void readResponse() throws IOException, InterruptedException {
        try {
            BufferedReader request = new BufferedReader(new InputStreamReader(
                    client.getInputStream()));
            BufferedWriter response = new BufferedWriter(
                    new OutputStreamWriter(client.getOutputStream()));

            String requestHeader = "";
            String temp = ".";
            while (!temp.equals("")) {
                temp = request.readLine();
                requestHeader += temp + "\n";
            }

            if (requestHeader.split("\n")[0].contains("GET")) {
                if (List.of(requestHeader.split("\n"))
                        .contains("QUESTION: CONNECT_NEW_CLIENT")
                ) {
                    System.out.println("NEW CLIENT WANTS TO CONNECT");
                }

                StringBuilder sb = new StringBuilder();
                sb.append("HTTP/1.1 200 OK\r\n");
                sb.append("Server: localhost\r\n");
                sb.append("Content-Type: json\r\n");
                sb.append("Connection: Closed\r\n\r\n");
                response.write(sb.toString());
                sb.setLength(0);
                response.flush();
            }

            request.close();
            response.close();
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}