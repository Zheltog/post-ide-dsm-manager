package su.nsk.iae.post.dsm.manager;

import org.junit.Test;
import org.mockito.Mockito;
import su.nsk.iae.post.dsm.manager.client.DSMMClient;
import su.nsk.iae.post.dsm.manager.client.DSMMClientLauncher;
import su.nsk.iae.post.dsm.manager.server.DSMMServerLauncher;
import static java.lang.Thread.sleep;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ServerLauncherTest {

    @Test
    public void testConnection() {
        try {
            DSMMClient client = mock(DSMMClient.class);

            new Thread(() -> {
                DSMMServerLauncher sl = new DSMMServerLauncher();
                sl.start("127.0.0.1", 8080);
            }).start();

            sleep(3000);

            new Thread(() -> {
                DSMMClientLauncher cl = new DSMMClientLauncher(client);
                cl.start("127.0.0.1", 8080, "127.0.0.1", 8085);
            }).start();

            sleep(3000);

            verify(client, times(1)).start(any());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}