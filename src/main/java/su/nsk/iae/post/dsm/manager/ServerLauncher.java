package su.nsk.iae.post.dsm.manager;

public class ServerLauncher {
	
    public void start(final String host, final int port) throws Exception {
    	System.out.println("PoST IDE DSM-manager: ServerLauncher:\tstarting on " + host + ":" + port + "...");
    	throw new Exception("oops!");
    }
    
    public void shutdown() {
    	System.out.println("PoST IDE DSM-manager: ServerLauncher:\tclosing...");
    }
}