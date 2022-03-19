package su.nsk.iae.post.dsm.manager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.Channels;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.eclipse.lsp4j.jsonrpc.Launcher;

public class ServerLauncher {
	private ServerThread serverThread;
	
    public void start(final String host, final int port) {
    	DSMManagerLogger.info(ServerLauncher.class, "starting on " + host + ":" + port + "...");
    	serverThread = new ServerThread(host, port);
    	serverThread.start();
    }
    
    public void shutdown() {
    	if (serverThread != null) {
    		serverThread.shutdown();
    	}
    }

	public DSMManager getManager() {
		return this.serverThread.manager;
	}
    
    private static final class ServerThread extends Thread {
    	private final String host;
    	private final int port;
    	
    	private boolean shouldRun = true;
    	private ExecutorService threadPool;
    	private AsynchronousSocketChannel socketChannel;
		private DSMManager manager;
    	
    	ServerThread(final String host, final int port) {
    		super("DSM-manager server");
    		this.host = host;
    		this.port = port;
    	}
    	
    	@Override
    	public void run() {
    		threadPool = Executors.newCachedThreadPool();
    		
    		try (AsynchronousServerSocketChannel serverSocket = AsynchronousServerSocketChannel
    				.open()
    				.bind(new InetSocketAddress(host, port))) {
    			
    			while (shouldRun) {
        			DSMManagerLogger.info(ServerLauncher.class, "waiting for request...");
    				socketChannel = serverSocket.accept().get();
					DSMManagerLogger.info(ServerLauncher.class, "got request...");
    				this.manager = new DSMManager();
    				final InputStream input = Channels.newInputStream(socketChannel);
    				final OutputStream output = Channels.newOutputStream(socketChannel);
    				final Launcher<DSMManagerClient> launcher = Launcher.createIoLauncher(
							manager, DSMManagerClient.class,
                            input, output, threadPool, msg -> msg
					);
    				final DSMManagerClient client = launcher.getRemoteProxy();
					manager.connect(client);
    				CompletableFuture.supplyAsync(() -> startLauncher(launcher)).thenRun(manager::dispose);
					DSMManagerLogger.info(ServerLauncher.class, "connected client " + socketChannel.getRemoteAddress());
    			}
    		} catch (Exception e) {
    			DSMManagerLogger.error(ServerLauncher.class, "got error at accepting new client...");
				DSMManagerLogger.error(ServerLauncher.class, e.getMessage());
    			e.printStackTrace();
    		}
    	}
    	
    	private Void startLauncher(final Launcher<DSMManagerClient> launcher) {
            try {
            	return launcher.startListening().get();
            } catch (InterruptedException | ExecutionException e) {
				DSMManagerLogger.error(ServerLauncher.class, "got error at accepting new client...");
				DSMManagerLogger.error(ServerLauncher.class, e.getMessage());
            	e.printStackTrace();
            }
            return null;
        }
    	
    	private void shutdown() {
    		this.shouldRun = false;
    		
    		if (socketChannel != null) {
    			try {
    				socketChannel.close();
					DSMManagerLogger.info(ServerLauncher.class, "closing...");
    			} catch (final IOException e) {
    				DSMManagerLogger.error(ServerLauncher.class, "got exception...");
					DSMManagerLogger.error(ServerLauncher.class, e.getMessage());
    				e.printStackTrace();
    			}
    			if (threadPool != null) {
    				threadPool.shutdownNow();
    			}
    		} else {
    			DSMManagerLogger.info(ServerLauncher.class, "got no socket channel...");
    		}
    	}
    }
}