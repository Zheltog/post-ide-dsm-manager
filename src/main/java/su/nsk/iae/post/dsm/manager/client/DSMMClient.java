package su.nsk.iae.post.dsm.manager.client;

import org.eclipse.lsp4j.jsonrpc.services.JsonNotification;
import org.eclipse.lsp4j.jsonrpc.services.JsonSegment;
import su.nsk.iae.post.dsm.manager.server.DSMMServer;

@JsonSegment("client")
public interface DSMMClient {

    @JsonNotification
    void removeFromServer(DSMMServer server);
}