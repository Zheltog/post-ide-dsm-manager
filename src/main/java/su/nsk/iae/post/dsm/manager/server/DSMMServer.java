package su.nsk.iae.post.dsm.manager.server;

import su.nsk.iae.post.dsm.manager.common.Message;

public interface DSMMServer {

    void notifyAll(Message message);
}