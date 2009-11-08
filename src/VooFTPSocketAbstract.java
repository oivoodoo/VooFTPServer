import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;


public abstract class VooFTPSocketAbstract extends Thread {
  protected Socket socket;
  protected VooFTPServer parent;
  protected BufferedReader reader = null;
  protected PrintWriter writer = null;
  protected char buffer[] = new char[FTPConstants.CHUNK_SIZE];
  protected Logger logger = Logger.getLogger(VooFTPSocketAbstract.class.getName());
  protected String id;

  public VooFTPSocketAbstract(VooFTPServer parent, Socket socket) throws Exception {
    this.socket = socket;
    this.parent = parent;
    logger.info(String.format("Local address is %s, local port is %s", socket.getLocalAddress().getHostAddress(), Integer.toString(socket.getLocalPort())));
    this.id = VooServerHelper.getUniqueId(socket.getLocalAddress().getHostAddress(), Integer.toString(socket.getLocalPort()));
    logger.info(String.format("Generated id is %s", id));

    if (!socket.isClosed() && socket.isConnected()) {
      InputStream inputStream = socket.getInputStream();
      OutputStream outputStream = socket.getOutputStream();
      if (inputStream != null) {
        reader = new BufferedReader(new InputStreamReader(inputStream));
      }
      if (outputStream != null) {
        writer = new PrintWriter(new OutputStreamWriter(outputStream));
      }
    } else {
      throw new Exception("Socket is closed");
    }
  }

  public void send(String line) throws Exception {
    if (writer != null) {
      writer.write(line);
      writer.flush();
    } else {
      throw new Exception("You haven't writer for this connection");
    }
  }

  public String getCommand(String line) {
    return line.split(" ")[0].replace("\r\n", "");
  }

  public String getValue(String line) throws Exception {
    int index = line.indexOf(" ");
    if (index != -1) {
      return line.substring(index + 1).replaceAll("\r\n", "");
    } else {
      throw new Exception("This command doesn't contain any values. Please ensure your protocol works correctly");
    }
  }
}
