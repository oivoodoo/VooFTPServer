import java.io.File;
import java.util.logging.Logger;

public class FTPSession {
	private String id;
    private String location;
	private VooFTPSocketData dataSocket;
	private VooFTPSocketControl controlSocket;
	public static Object sync = new Object();
    protected Logger logger = Logger.getLogger(FTPSession.class.getName());
	
	public FTPSession(String id) {
		this.id = id;
	}
	
	public FTPSession(String id, VooFTPSocketData dataSocket, VooFTPSocketControl controlSocket) {
		this.id = id;
		this.dataSocket = dataSocket;
		this.controlSocket = controlSocket;
	}
	
	public void close() throws Exception {
		if (dataSocket != null) {
			dataSocket.close();
		}
		if (controlSocket != null) {
			controlSocket.close();
		}
	}

    public String upLocation() {
        if (location.split("/").length > 1) {
            int index = location.lastIndexOf("/");
            location.subSequence(0, index);
        }
        return location;
    }

    public String intoLocation(String relativeDirectory) throws Exception {
        if (relativeDirectory.lastIndexOf("/") == relativeDirectory.length() - 1) {
            // remove last '/'
            relativeDirectory = relativeDirectory.substring(0, relativeDirectory.length() - 1);
        }
        String temp = location + "/" + relativeDirectory;
        File file = new File(temp);
        if (file.isDirectory()) {
            location = temp;
        } else {
            throw new Exception("Not valid folder");
        }
        return location;
    }

    public void makeFolder(String location) {
        File directory = new File(getLocation() + "/" + location);
        if (directory.mkdir()) {
            logger.info(String.format("Make folder %s", directory.getAbsolutePath()));
        } else {
            logger.info(String.format("Can't make folder %s", directory.getAbsolutePath()));
        }
    }

    public void deleteFolder(String location) {
        File file = new File(getLocation() + "/" + location);
        if (file.isDirectory() && file.exists()) {
            if (file.delete()) {
                logger.info(String.format("Remove folder %s", file.getAbsolutePath()));
            } else {
                logger.info(String.format("Can't remove folder %s", file.getAbsolutePath()));
            }
        }
    }

    public void delete(String location) {
        File file = new File(getLocation() + "/" + location);
        if (file.exists()) {
            if (file.delete()) {
                logger.info(String.format("%s deleted", location));
            } else {
                logger.info(String.format("%s can't be deleted", location));
            }
        }
    }
	
	public VooFTPSocketControl getControlSocket() {
		return this.controlSocket;
	}
	
	public VooFTPSocketData getDataSocket() {
		return this.dataSocket;
	}

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return this.location;
    }

	public void setControlSocket(VooFTPSocketControl control) {
		this.controlSocket = control;
	}
	
	public void setDataSocket(VooFTPSocketData data) {
		this.dataSocket = data;
	}
	
	public String getId() {
		return id;
	}
}
