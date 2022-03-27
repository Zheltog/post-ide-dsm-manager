package su.nsk.iae.post.dsm.manager.server;

import su.nsk.iae.post.dsm.manager.common.DSMMLogger;
import su.nsk.iae.post.dsm.manager.client.DSMMClient;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DSMMServerImpl implements DSMMServer {

    private final List<DSMMClient> clients = new CopyOnWriteArrayList<>();

    public List<DSMMClient> getClients() {
        return clients;
    }

    /**
     * add new dsm-manager client
     * @param client client to add
     * @return action that removes this client
     */
    public Runnable addClient(DSMMClient client) {
        this.clients.add(client);
        DSMMLogger.info(DSMMServerImpl.class, "added new client");
        return () -> {
            this.clients.remove(client);
            DSMMLogger.info(DSMMServerImpl.class, "client removed");
        };
    }
}