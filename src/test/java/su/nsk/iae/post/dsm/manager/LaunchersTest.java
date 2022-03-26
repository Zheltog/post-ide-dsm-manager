package su.nsk.iae.post.dsm.manager;

import org.junit.Test;
import su.nsk.iae.post.dsm.manager.client.DSMMClient;
import su.nsk.iae.post.dsm.manager.client.DSMMClientLauncher;
import su.nsk.iae.post.dsm.manager.server.DSMMServer;
import su.nsk.iae.post.dsm.manager.server.DSMMServerImpl;
import su.nsk.iae.post.dsm.manager.server.DSMMServerLauncher;
import static java.lang.Thread.sleep;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class LaunchersTest {

    @Test
    public void testConnection() {
        try {
            DSMMServer server = new DSMMServerImpl();

            new Thread(() -> {
                DSMMServerLauncher sl = new DSMMServerLauncher(server);
                sl.start("127.0.0.1", 8080);
            }).start();

            sleep(3000);

            DSMMClient client = mock(DSMMClient.class);

            new Thread(() -> {
                DSMMClientLauncher cl = new DSMMClientLauncher(client);
                cl.start("127.0.0.1", 8080);
            }).start();

            sleep(3000);

            verify(client, times(1)).start(any());
            assertEquals(server.getClients().size(), 1);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}