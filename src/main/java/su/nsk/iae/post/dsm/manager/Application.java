package su.nsk.iae.post.dsm.manager;

public class Application {
	
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 8080;
    
    private static ServerLauncher serverLauncher;
	
	public static void main(String[] args) {
		DSMManagerLogger.info(Application.class, "use -help to see available running configurations");
		
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
			
			DSMManagerLogger.info(Application.class, "input anything to stop server...");
			System.in.read();
			stop();
		} catch (Exception e) {
			DSMManagerLogger.error(Application.class, "got exception...");
			DSMManagerLogger.error(Application.class, e.getMessage());
			e.printStackTrace();
			stop();
		}
	}
	
	private static void help() {
		DSMManagerLogger.info(Application.class, "available running configurations:");
		DSMManagerLogger.info(Application.class, "-host (default \"localhost\")");
		DSMManagerLogger.info(Application.class, "-port (default 8080)");
	}
	
	private static void stop() {
		DSMManagerLogger.info(Application.class, "closing server...");
		
		if (serverLauncher != null) {
			serverLauncher.shutdown();
			serverLauncher = null;
		}
	}
}