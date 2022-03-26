package su.nsk.iae.post.dsm.manager.server;

import su.nsk.iae.post.dsm.manager.common.DSMMLogger;
import su.nsk.iae.post.dsm.manager.common.Message;
import su.nsk.iae.post.dsm.manager.client.DSMMClient;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DSMMServerImpl implements DSMMServer {

    private final List<DSMMClient> clients = new CopyOnWriteArrayList<>();

    public void notifyAll(Message message) {
        DSMMLogger.info(DSMMServerImpl.class, "notifying everyone...");
        for (DSMMClient client : clients) {
            client.accept(message);
        }
    }

    public Runnable addClient(DSMMClient client) {
        DSMMLogger.info(DSMMServerImpl.class, "adding new client...");
        this.clients.add(client);
        return () -> this.clients.remove(client);
    }
}