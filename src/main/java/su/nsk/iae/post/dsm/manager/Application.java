package su.nsk.iae.post.dsm.manager;

public class Application {
	
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 8080;
    
    private static ServerLauncher serverLauncher;
	
	public static void main(String[] args) {
		System.out.println("PoST IDE DSM-manager: Application:\tuse -help to see available running configurations");
		
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
			}
		}
		
		try {
			serverLauncher = new ServerLauncher();
			serverLauncher.start(host, port);
			
			Runtime.getRuntime().addShutdownHook(
					new Thread(Application::stop)
			);
			
			System.out.println("PoST IDE DSM-manager: Application:\tinput anything to stop server...");
			System.in.read();
			stop();
		} catch (Exception e) {
			System.out.println("PoST IDE DSM-manager: Application:\tgot exception...");
			stop();
			e.printStackTrace();
		}
	}
	
	private static void help() {
		System.out.println("PoST IDE DSM-manager: Application:\tavailable running configurations:");
		System.out.println("\t-host (default \"localhost\")");
		System.out.println("\t-port (default 8080)");
	}
	
	private static void stop() {
		System.out.println("PoST IDE DSM-manager: Application:\tclosing server...");
		
		if (serverLauncher != null) {
			serverLauncher.shutdown();
			serverLauncher = null;
		}
	}
}