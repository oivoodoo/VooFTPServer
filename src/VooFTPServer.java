import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

// Class implements default statement for server with dual connections such as
// data connection and control connection for more information please see
// rfc documentation.
public class VooFTPServer implements Runnable, FTPServer {
	private Thread serverThread = new Thread(this);
	private VooFTPServerData serverData = new VooFTPServerData(this);
	private VooFTPServerControl serverControl = new VooFTPServerControl(this);
	private ArrayList<VooUser> users = new ArrayList<VooUser>();
	private HashMap<String, FTPSession> sessions = new HashMap<String, FTPSession>();
    private int controlPort = FTPConstants.FTP_PORT_CONTROL;
    private int dataPort = FTPConstants.FTP_PORT_DATA;
    private String location;

    // This method create server object with default ftp ports. 
	public VooFTPServer(String location) {
		this.location = location;
        initializeUsers();
	}

    // If you really want to use custom ports for your connections
    // please use this constructor.
    public VooFTPServer(String location, int controlPort, int dataPort) {
        this.location = location;
        this.controlPort = controlPort;
        this.dataPort = dataPort;
        initializeUsers();
    }

    // This is default intialization with users.
    // This is method can be rewrite for adding support with users
    // from database or another sources.
    private void initializeUsers() {
        users.add(new VooUser("admin", "123456"));
		users.add(new VooUser("oivoodoo", "123456"));
		users.add(new VooUser("vitalik", "123456"));
    }

    // Validations for users, this method generally uses in
    // the inner classes with socket control and data connections
    // for checking authentifications.
	public boolean isValidUser(VooUser user) {
		if (user != null) {
			for(VooUser value : users) {
				if (user.compareTo(value) == 0) {
					return true;
				}
			}
		}
		return false;
	}

	// Start thread with checking new connections.
    // serverData connections only uses if client doesn't
    // work with PORT ftp command for creating remote end point.
	@Override
	public void run() {
		serverData.start();
		serverControl.start();
	}

    // Start server ftp thread in another process.
	@Override
	public void start() {
        System.out.println("Start voo ftp server...");
		serverThread.start();
	}

    // Stop all threads.
	@Override
	public void stop() {
        System.out.println("Stoped voo ftp server...");
		serverData.stop();
		serverControl.stop();
		serverThread.interrupt();
	}
	
	public HashMap<String, FTPSession> getSessions() {
		return sessions;
	}
	
	public String getLocation() {
		return location;
	}
	
	public VooFTPServerData getServerData() {
		return this.serverData;
	}
	
	public VooFTPServerControl getServerControl() {
		return this.serverControl;
	}

    public int getDataPort() {
        return this.dataPort;
    }

    public int getControlPort() {
        return this.controlPort;
    }
}
