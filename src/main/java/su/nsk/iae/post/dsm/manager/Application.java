package su.nsk.iae.post.dsm.manager;

import su.nsk.iae.post.dsm.manager.common.DSMMLogger;
import su.nsk.iae.post.dsm.manager.server.DSMMServerLauncher;
import java.io.IOException;

public class Application {

	private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 8080;

	private static DSMMServerLauncher launcher;
	
	public static void main(String[] args) {
		DSMMLogger.info(
				Application.class,
				"use -help to see available running configurations"
		);

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

		launcher = new DSMMServerLauncher();
		final String finalHost = host;
		final int finalPort = port;

		new Thread(() -> launcher.start(finalHost, finalPort)).start();

		Runtime.getRuntime().addShutdownHook(
				new Thread(Application::stop)
		);

		try {
			DSMMLogger.info(Application.class, "any input for stop");
			System.in.read();
			stop();
		} catch (IOException e) {
			DSMMLogger.error(Application.class, e.getMessage());
		}
	}
	
	private static void help() {
		DSMMLogger.info(Application.class, "available running configurations:");
		DSMMLogger.info(Application.class, "-port (default 8080)");
	}

	private static void stop() {
		launcher.stop();
	}
}