package su.nsk.iae.post.dsm.manager.client;

import su.nsk.iae.post.dsm.manager.common.Message;
import su.nsk.iae.post.dsm.manager.server.DSMMServer;

public interface DSMMClient {

    void start(DSMMServer server);

    void accept(Message message);
}