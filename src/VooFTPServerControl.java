import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;


public class VooFTPServerControl extends VooFTPServerAbstract  {
	
	public VooFTPServerControl(VooFTPServer parent) {
		super(parent);
	}
	
	@Override
	public void run() {
		try {
			server = new ServerSocket(FTPConstants.FTP_PORT_CONTROL);
			while(true) {
                Socket socket = server.accept();
                logger.info("ServerControl: server accept new socket connection.");
                String id = VooServerHelper.getUniqueId(socket.getLocalAddress().getHostAddress(),
                        Integer.toString(socket.getLocalPort()));
                logger.info(String.format("Generate new id - %s", id));
                VooFTPSocketControl control = new VooFTPSocketControl(parent, socket);
                synchronized (FTPSession.sync) {
                    logger.info(String.format("Create new ftp session with id - %s", id));
					if (!parent.getSessions().containsKey(id)) {
						parent.getSessions().put(id, new FTPSession(id));
                        parent.getSessions().get(id).setLocation(parent.getLocation());
					} else {
						parent.getSessions().get(id).setControlSocket(control);
					}
				}
				control.start();
			}
		} catch (Exception e) {
            logger.severe(e.getMessage());
			e.printStackTrace();
		}
	}
}
