package su.nsk.iae.post.dsm.manager.client;

import su.nsk.iae.post.dsm.manager.common.DSMMLogger;

public class DSMMClientImpl implements DSMMClient {

    public void start() {
        DSMMLogger.info(DSMMClientImpl.class, "starting...");
    }
}