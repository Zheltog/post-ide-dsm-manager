package su.nsk.iae.post.dsm.manager.client;

import su.nsk.iae.post.dsm.manager.common.Message;

public interface DSMMClient {

    void accept(Message message);
}