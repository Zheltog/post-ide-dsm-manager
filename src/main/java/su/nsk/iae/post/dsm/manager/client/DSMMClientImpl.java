package su.nsk.iae.post.dsm.manager.client;

import su.nsk.iae.post.dsm.manager.common.Message;
import su.nsk.iae.post.dsm.manager.server.DSMMServer;

public class DSMMClientImpl implements DSMMClient {

    @Override
    public void start(DSMMServer server) {
        server.notifyAll(new Message("test"));
    }

    @Override
    public void accept(Message message) {
        System.out.println(message.content);
    }
}