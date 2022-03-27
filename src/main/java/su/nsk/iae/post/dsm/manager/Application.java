package su.nsk.iae.post.dsm.manager;

import su.nsk.iae.post.dsm.manager.common.Logger;
import su.nsk.iae.post.dsm.manager.server.Server;
import java.io.IOException;

public class Application {

    private static final int DEFAULT_PORT = 8080;
	
	public static void main(String[] args) {
		Logger.info(
				Application.class,
				"use -help to see available running configurations"
		);

		int port = DEFAULT_PORT;

		for (int i = 0; i < args.length; i++) {
			switch (args[i]) {
				case "-help":
					help();
					return;
				case "-port":
					port = Integer.parseInt(args[++i]);
			}
		}

		Server server = new Server(port);
		server.start();

		Runtime.getRuntime().addShutdownHook(new Thread(server::stop));

		try {
			Logger.info(Application.class, "any input for stop");
			System.in.read();
			server.stop();
		} catch (IOException e) {
			Logger.error(Application.class, e.getMessage());
		}
	}
	
	private static void help() {
		Logger.info(Application.class, "available running configurations:");
		Logger.info(Application.class, "-port (default 8080)");
	}
}