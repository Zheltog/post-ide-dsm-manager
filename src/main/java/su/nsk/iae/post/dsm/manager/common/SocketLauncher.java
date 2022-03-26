package su.nsk.iae.post.dsm.manager.common;

import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.jsonrpc.RemoteEndpoint;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

public class SocketLauncher<T> implements Launcher<T> {

    private final Launcher<T> launcher;

    public SocketLauncher(Object localService, Class<T> remoteInterface, Socket socket) {
        try {
            this.launcher = Launcher.createLauncher(
                    localService, remoteInterface,
                    socket.getInputStream(), socket.getOutputStream()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CompletableFuture<Void> startListening() {
        return CompletableFuture.runAsync(
                () -> {
                    try {
                        this.launcher.startListening().get();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                },
                Executors.newSingleThreadExecutor()
        );
    }

    @Override
    public T getRemoteProxy() { return this.launcher.getRemoteProxy(); }

    @Override
    public RemoteEndpoint getRemoteEndpoint() { return null; }
}