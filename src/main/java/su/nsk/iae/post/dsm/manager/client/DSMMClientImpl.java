package su.nsk.iae.post.dsm.manager.client;

import su.nsk.iae.post.dsm.manager.common.DSMMLogger;
import su.nsk.iae.post.dsm.manager.server.DSMMServer;

public class DSMMClientImpl implements DSMMClient {

    @Override
    public void start(DSMMServer server) {
        DSMMLogger.info(DSMMClientImpl.class, "starting...");
    }
}