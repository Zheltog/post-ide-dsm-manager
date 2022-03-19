package su.nsk.iae.post.dsm.manager;

public class DSMManager {

	public boolean isClientConnected = false;
	
	public void connect(DSMManagerClient client) {
		isClientConnected = true;
	}
	
	public void dispose() {}
}