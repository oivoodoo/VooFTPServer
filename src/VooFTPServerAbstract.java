import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.logging.Logger;


public abstract class VooFTPServerAbstract implements Runnable, FTPServer {
	private Thread thread = new Thread(this);
	protected ServerSocket server;
	protected VooFTPServer parent;
	protected BufferedReader reader = null;
	protected PrintWriter writer = null;
	protected char buffer[] = new char[FTPConstants.CHUNK_SIZE];
	protected Logger logger = Logger.getLogger(VooFTPServerAbstract.class.getName());
	
	public VooFTPServerAbstract(VooFTPServer parent) {
		this.parent = parent;
	}
	
	@Override
	public void start() {
		thread.start();
	}

	@Override
	public void stop() {
		thread.interrupt();		
	}
}
