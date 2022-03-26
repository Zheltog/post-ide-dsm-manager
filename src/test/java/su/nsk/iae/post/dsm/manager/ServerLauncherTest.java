package su.nsk.iae.post.dsm.manager;

import org.junit.Test;
import org.mockito.Mockito;
import su.nsk.iae.post.dsm.manager.client.DSMMClient;
import su.nsk.iae.post.dsm.manager.client.DSMMClientLauncher;
import su.nsk.iae.post.dsm.manager.server.DSMMServerLauncher;

public class ServerLauncherTest {

    @Test
    public void testConnection() {
        try {
            DSMMClient client = Mockito.mock(DSMMClient.class);

            DSMMServerLauncher sl = new DSMMServerLauncher();
            sl.start(8080);

            DSMMClientLauncher cl = new DSMMClientLauncher(client);
            cl.start("0.0.0.0", 8080);

            Mockito.verify(client, Mockito.times(1))
                    .accept(Mockito.any());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}