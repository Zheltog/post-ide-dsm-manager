package su.nsk.iae.post.dsm.manager.client;

import su.nsk.iae.post.dsm.manager.common.DSMMLogger;
import su.nsk.iae.post.dsm.manager.server.DSMMServer;

public class DSMMClientImpl implements DSMMClient {

    private int index = -1;

    public void start() {
        DSMMLogger.info(DSMMClientImpl.class, "starting...");
    }

    @Override
    public void setIndex(int index) {
        this.index = index;
        DSMMLogger.info(DSMMClientImpl.class, "set index = " + index);
    }

    @Override
    public void removeFromServer(DSMMServer server) {
        server.removeClient(index);
    }
}