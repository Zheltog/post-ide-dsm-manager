package su.nsk.iae.post.dsm.manager.server;

import su.nsk.iae.post.dsm.manager.common.DSMMLogger;
import su.nsk.iae.post.dsm.manager.client.DSMMClient;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import static java.util.concurrent.CompletableFuture.completedFuture;

public class DSMMServerImpl implements DSMMServer {

    private final List<DSMMClient> clients = new CopyOnWriteArrayList<>();

    @Override
    public CompletableFuture<List<DSMMClient>> getClients() {
        return completedFuture(clients);
    }

    @Override
    public void addClient(DSMMClient client) {
        this.clients.add(client);
        DSMMLogger.info(DSMMServerImpl.class, "added new client");
    }

    @Override
    public CompletableFuture<Void> removeClient(int clientIndex) {
        this.clients.remove(clientIndex);
        DSMMLogger.info(DSMMServerImpl.class, "client removed");
        return completedFuture(null);
    }
}