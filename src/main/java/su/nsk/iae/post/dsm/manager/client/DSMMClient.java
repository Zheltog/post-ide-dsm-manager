package su.nsk.iae.post.dsm.manager.client;

import org.eclipse.lsp4j.jsonrpc.services.JsonNotification;
import org.eclipse.lsp4j.jsonrpc.services.JsonSegment;

@JsonSegment("client")
public interface DSMMClient {

    @JsonNotification
    void removeFromServer();
}