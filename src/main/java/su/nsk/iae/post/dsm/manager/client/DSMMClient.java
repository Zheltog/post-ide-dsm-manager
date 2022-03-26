package su.nsk.iae.post.dsm.manager.client;

import su.nsk.iae.post.dsm.manager.server.DSMMServer;

public interface DSMMClient {

    void start(DSMMServer server);
}