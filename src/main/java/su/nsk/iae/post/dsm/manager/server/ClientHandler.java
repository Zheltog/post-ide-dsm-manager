package su.nsk.iae.post.dsm.manager.server;

import com.google.gson.JsonObject;
import org.eclipse.lsp4j.jsonrpc.validation.NonNull;
import su.nsk.iae.post.dsm.manager.common.Logger;
import su.nsk.iae.post.dsm.manager.common.Request;
import su.nsk.iae.post.dsm.manager.common.Response;
import su.nsk.iae.post.dsm.manager.common.ServerUtils;
import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import static su.nsk.iae.post.dsm.manager.common.Response.ResponseType.*;

public class ClientHandler implements Runnable {

    private final Socket client;

    public ClientHandler(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            logInfo("started");
            BufferedReader request = new BufferedReader(new InputStreamReader(client.getInputStream()));
            BufferedWriter response = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            process(request, response, getContent(request));
        } catch (IOException e) {
            logError(e.getMessage());
        }
    }

    private List<String> getContent(BufferedReader request) throws IOException {
        logInfo("reading request content...");
        List<String> content = new LinkedList<>();
        String str = ".";
        while (!str.equals("")) {
            str = request.readLine();
            content.add(str);
        }
        return content;
    }

    private void process(
            BufferedReader requestReader,
            BufferedWriter responseWriter,
            List<String> requestContent
    ) throws IOException {
        logInfo("processing...");
        if (requestContent.get(0).contains("GET")) {
            for (Request request : Request.values()) {
                if (requestContent.contains("REQUEST: " + request)) {
                    responseWriter.write("HTTP/1.1 200 OK\r\nContent-Type: application/json\r\n\r\n");
                    responseWriter.write(responseFor(request).toString());
                    responseWriter.flush();
                    finish(requestReader, responseWriter, client);
                    return;
                }
            }
            responseWriter.write("HTTP/1.1 404 NOT FOUND\r\n\r\n");
            responseWriter.flush();
            finish(requestReader, responseWriter, client);
        }
    }

    @NonNull
    private JsonObject responseFor(Request request) {
        logInfo("preparing response for request: " + request + "...");
        JsonObject json = new JsonObject();
        switch (request) {
            case NEW_MODULE:
                Response a = new Response(FREE_PORT, ServerUtils.findFreePort());
                json.addProperty(a.getType().toString(), (Integer) a.getValue());
                break;
        }
        logInfo("built response: " + json);
        return json;
    }

    private void finish(
            BufferedReader request,
            BufferedWriter response,
            Socket client
    ) throws IOException {
        logInfo("finishing...");
        request.close();
        response.close();
        client.close();
    }

    private void logInfo(String message) {
        Logger.info(ClientHandler.class, message);
    }

    private void logError(String message) {
        Logger.error(ClientHandler.class, message);
    }
}