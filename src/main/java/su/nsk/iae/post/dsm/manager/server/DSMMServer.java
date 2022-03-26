package su.nsk.iae.post.dsm.manager.server;

import su.nsk.iae.post.dsm.manager.common.Message;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface DSMMServer {

    CompletableFuture<List<Message>> fetchMessages();

    void postMessage(Message message);
}