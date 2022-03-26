package su.nsk.iae.post.dsm.manager;

import org.junit.Test;
import su.nsk.iae.post.dsm.manager.client.DSMMClientImpl;
import su.nsk.iae.post.dsm.manager.client.DSMMClientLauncher;
import su.nsk.iae.post.dsm.manager.server.DSMMServer;
import su.nsk.iae.post.dsm.manager.server.DSMMServerImpl;
import su.nsk.iae.post.dsm.manager.server.DSMMServerLauncher;
import static java.lang.Thread.sleep;
import static org.junit.Assert.*;

public class LaunchersTest {

    @Test
    public void testCommunication() {
        try {
            DSMMServer server = new DSMMServerImpl();
            DSMMClientImpl client = new DSMMClientImpl();
            DSMMClientLauncher cl = new DSMMClientLauncher(client);

            new Thread(() -> {
                DSMMServerLauncher sl = new DSMMServerLauncher(server);
                sl.start("127.0.0.1", 8080);
            }).start();
            sleep(2000);

            new Thread(() -> cl.start("127.0.0.1", 8080))
                    .start();
            sleep(2000);

            assertEquals(server.getClients().get().size(), 1);
            cl.stop();
            sleep(2000);
//            assertEquals(server.getClients().get().size(), 0);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}