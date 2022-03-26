package su.nsk.iae.post.dsm.manager.client;

import su.nsk.iae.post.dsm.manager.common.DSMMLogger;
import su.nsk.iae.post.dsm.manager.server.DSMMServer;

public class DSMMClientImpl implements DSMMClient {

    public void start() {
        DSMMLogger.info(DSMMClientImpl.class, "starting...");
    }

    @Override
    public void removeFromServer(DSMMServer server) {
        server.removeClient(5);
    }
}