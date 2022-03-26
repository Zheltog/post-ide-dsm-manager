package su.nsk.iae.post.dsm.manager.server;

import org.eclipse.lsp4j.jsonrpc.services.JsonNotification;
import org.eclipse.lsp4j.jsonrpc.services.JsonRequest;
import org.eclipse.lsp4j.jsonrpc.services.JsonSegment;
import su.nsk.iae.post.dsm.manager.client.DSMMClient;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@JsonSegment("server")
public interface DSMMServer {

    @JsonRequest
    CompletableFuture<List<DSMMClient>> getClients();

    @JsonNotification
    void addClient(DSMMClient client);

    @JsonRequest
    CompletableFuture<Void> removeClient(int clientIndex);
}