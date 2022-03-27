package su.nsk.iae.post.dsm.manager;

import org.junit.Test;
import org.mockito.Mockito;
import su.nsk.iae.post.dsm.manager.client.DSMMClientImpl;
import su.nsk.iae.post.dsm.manager.client.DSMMClientLauncher;
import su.nsk.iae.post.dsm.manager.server.DSMMServerImpl;
import su.nsk.iae.post.dsm.manager.server.DSMMServerLauncher;
import static java.lang.Thread.sleep;
import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class LaunchersTest {

    @Test
    public void testCommunicationWithTwoClients() {
        try {
            DSMMServerImpl server = new DSMMServerImpl();
            DSMMClientImpl client1 = Mockito.mock(DSMMClientImpl.class);
            DSMMClientImpl client2 = Mockito.mock(DSMMClientImpl.class);
            DSMMClientLauncher cl1 = new DSMMClientLauncher(client1);
            DSMMClientLauncher cl2 = new DSMMClientLauncher(client2);

            new Thread(() -> {
                DSMMServerLauncher sl = new DSMMServerLauncher(server);
                sl.start("127.0.0.1", 8080);
            }).start();
            sleep(2000);

            new Thread(() -> cl1.start("127.0.0.1", 8080))
                    .start();
            sleep(2000);

            new Thread(() -> cl2.start("127.0.0.1", 8080))
                    .start();
            sleep(2000);

            verify(client1, times(1)).start();
            verify(client2, times(1)).start();

            assertEquals(server.getClients().size(), 2);
            cl1.stop();
            sleep(2000);
            assertEquals(server.getClients().size(), 1);
            cl2.stop();
            sleep(2000);
            assertEquals(server.getClients().size(), 0);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}