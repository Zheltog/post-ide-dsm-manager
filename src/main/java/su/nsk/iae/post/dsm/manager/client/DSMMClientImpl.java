package su.nsk.iae.post.dsm.manager.client;

import su.nsk.iae.post.dsm.manager.common.DSMMLogger;
import su.nsk.iae.post.dsm.manager.server.DSMMServer;

public class DSMMClientImpl implements DSMMClient {

    private DSMMServer server;

    public void start(DSMMServer server) {
        DSMMLogger.info(DSMMClientImpl.class, "starting...");
        this.server = server;
    }

    @Override
    public void removeFromServer() {
        if (server != null) {
            server.removeClient(this);
        }
    }
}