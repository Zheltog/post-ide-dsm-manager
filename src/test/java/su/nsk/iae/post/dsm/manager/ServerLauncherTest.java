package su.nsk.iae.post.dsm.manager;

import org.junit.Test;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ServerLauncherTest {

    @Test
    public void testConnection() {
        try {
            ServerLauncher sl = new ServerLauncher();
            sl.start("127.0.0.1", 8080);

            AsynchronousSocketChannel socketChannel = AsynchronousSocketChannel.open().bind(null);
            InetSocketAddress hostAddress = new InetSocketAddress("127.0.0.1", 8080);
            Future<Void> connection = socketChannel.connect(hostAddress);
            connection.get();

            DSMManager manager = null;
            while (manager == null) {
                manager = sl.getManager();
                Thread.sleep(3000);
            }

            assertTrue(manager.isClientConnected);
        } catch (IOException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
            fail();
        }
    }
}