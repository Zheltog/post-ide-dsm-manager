package su.nsk.iae.post.dsm.manager.server;

import su.nsk.iae.post.dsm.manager.client.DSMMClient;
import java.util.List;

public interface DSMMServer {

    List<DSMMClient> getClients();

    void addClient(DSMMClient client);
}