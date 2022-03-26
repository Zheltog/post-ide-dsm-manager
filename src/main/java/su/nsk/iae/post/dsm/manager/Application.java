package su.nsk.iae.post.dsm.manager;

import su.nsk.iae.post.dsm.manager.common.DSMMLogger;
import su.nsk.iae.post.dsm.manager.server.DSMMServerLauncher;

public class Application {

	private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 8080;
	
	public static void main(String[] args) {
		DSMMLogger.info(Application.class, "use -help to see available running configurations");

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

		new DSMMServerLauncher().start(host, port);
	}
	
	private static void help() {
		DSMMLogger.info(Application.class, "available running configurations:");
		DSMMLogger.info(Application.class, "-port (default 8080)");
	}
}