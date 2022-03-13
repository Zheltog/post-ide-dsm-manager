package su.nsk.iae.post.dsm.manager;

public class Application {
	
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 8080;
	
	public static void main(String[] args) {
		System.out.println("PoST IDE DSM-manager:\tuse -help to see available running configurations");
		
	    String host = DEFAULT_HOST;
	    int port = DEFAULT_PORT;
	    
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            switch (arg) {
            	case "-help":
            		help();
            		return;
                case "-port":
                	i++;
                    port = Integer.parseInt(args[i]);
                    break;
                case "-host":
                	i++;
                    host = args[i];
                    break;
            }
        }
        
        System.out.println("PoST IDE DSM-manager:\tstarting on " + host + ":" + port + "...");
	}
	
	private static void help() {
		System.out.println("PoST IDE DSM-manager:\tavailable running configurations:");
		System.out.println("\t-host (default \"localhost\")");
		System.out.println("\t-port (default 8080)");
	}
}