import java.net.ServerSocket;
import java.net.Socket;


public class VooFTPServerData  extends VooFTPServerAbstract  {
	public VooFTPServerData(VooFTPServer parent) {
		super(parent);
	}
	
	@Override
	public void run() {
		try {
			server = new ServerSocket(FTPConstants.FTP_PORT_DATA);
			while(true) {
				Socket socket = server.accept();
                logger.info("ServerData: server accept new socket connection.");
				String id = VooServerHelper.getUniqueId(socket.getLocalAddress().getHostAddress(), Integer.toString(socket.getLocalPort()));
                logger.info(String.format("ServerData: new connection with id - %s", id));
				VooFTPSocketData data = new VooFTPSocketData(parent, socket);
				synchronized (FTPSession.sync) {
					if (!parent.getSessions().containsKey(id)) {
						parent.getSessions().put(id, new FTPSession(id));
					} else {
						parent.getSessions().get(id).setDataSocket(data);
					}
				}
				data.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
