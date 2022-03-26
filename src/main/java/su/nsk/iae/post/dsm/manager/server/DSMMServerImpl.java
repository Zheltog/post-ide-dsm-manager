package su.nsk.iae.post.dsm.manager.server;

import su.nsk.iae.post.dsm.manager.common.Message;
import su.nsk.iae.post.dsm.manager.client.DSMMClient;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

public class DSMMServerImpl implements DSMMServer {

    private final List<Message> messages = new CopyOnWriteArrayList<>();
    private final List<DSMMClient> clients = new CopyOnWriteArrayList<>();

    public CompletableFuture<List<Message>> fetchMessages() {
        return CompletableFuture.completedFuture(messages);
    }

    public void postMessage(Message message) {
        messages.add(message);
        for (DSMMClient client : clients) {
            client.accept(message);
        }
    }

    public Runnable addClient(DSMMClient client) {
        this.clients.add(client);
        return () -> this.clients.remove(client);
    }
}