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
    	System.out.println("PoST IDE DSM-manager: ServerLauncher:\tstarting on " + host + ":" + port + "...");
    	serverThread = new ServerThread(host, port);
    	serverThread.start();
    }
    
    public void shutdown() {
    	if (serverThread != null) {
    		serverThread.shutdown();
    	}
    }
    
    private final class ServerThread extends Thread {
    	private final String host;
    	private final int port;
    	
    	private ExecutorService threadPool;
    	private boolean shouldRun = true;
    	private AsynchronousSocketChannel socketChannel;
    	
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
    				System.out.println("PoST IDE DSM-manager: ServerThread:\trunning...");
    			}
    		} catch (Exception e) {
    			System.out.println("PoST IDE DSM-manager: ServerThread:\tgot exception...");
    			e.printStackTrace();
    		}
    	}
    	
    	private void shutdown() {
    		this.shouldRun = false;
    		
    		if (socketChannel != null) {
    			try {
    				socketChannel.close();
    				System.out.println("PoST IDE DSM-manager: ServerThread:\\tclosing...");
    			} catch (final IOException e) {
    				System.out.println("PoST IDE DSM-manager: ServerThread:\tgot exception...");
    				e.printStackTrace();
    			}
    			if (threadPool != null) {
    				threadPool.shutdownNow();
    			}
    		} else {
    			System.out.println("PoST IDE DSM-manager: ServerThread:\tgot no socket channel...");
    		}
    	}
    }
}