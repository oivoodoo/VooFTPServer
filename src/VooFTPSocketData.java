import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

public class VooFTPSocketData extends VooFTPSocketAbstract {
    private String location;

	public VooFTPSocketData(VooFTPServer parent, Socket socket) throws Exception {
		super(parent, socket);
	}
    
	public void send(String location) {
        this.location = location;
        logger.info(String.format("Location: %s", location));
        writer.flush();
		File file = new File(location);
		if (file.exists()) {
            logger.info(String.format("%s exists", location));
			if (file.canRead()) {
                logger.info(String.format("%s can read", location));
                RandomAccessFile reader = null;
				try {
                    reader = new RandomAccessFile(location, "r");
					long size = 0;
                    logger.info(String.format("Send file from %s", location));
                    byte content[] = new byte[FTPConstants.CHUNK_SIZE];
                    OutputStream stream = socket.getOutputStream();
					while(size < file.length()) {
						int read = reader.read(content, 0, FTPConstants.CHUNK_SIZE);
						if(read != -1) {
                            logger.info(String.format("Send: Read %d bytes", read));
							size += read;
                            stream.write(content, 0, read);
                            stream.flush();
						} else {
                            break;
                        }
					}
				} catch(Exception ex) {
					logger.severe(ex.getMessage());
				} finally {
					if (reader != null) {
						try {
							reader.close();
						} catch(Exception ex) {
							logger.severe(ex.getMessage());
						}
					}
				}
			}
		}
	}
	
	public void receive(String location) throws Exception {
        this.location = location;
		int read = 0;
        OutputStreamWriter fileWriter = null;
        try {
            fileWriter = new OutputStreamWriter(new FileOutputStream(location), Charset.forName("US-ASCII"));
            while(read != -1) {
                read = reader.read(buffer);
                logger.info(String.format("Receive: Read %d bytes", read));
                if (read != -1) {
                    String data = VooServerHelper.toStringCommand(buffer, read);
                    logger.info(String.format("File Data: %s", data));
                    fileWriter.write(buffer, 0, read);
                }
            }
        } finally {
            if (fileWriter != null) {
                fileWriter.close();
            }
        }
	}

    public void receiveByOffset(int offset) throws Exception {
        if (location == null) throw new Exception("Location haven't set before.");
        logger.info(String.format("Try to receive file by offset - %d", offset));
        int read = 0;
        RandomAccessFile randomAccess = null;
        try {
            randomAccess = new RandomAccessFile(location, "r");
            long size = 0;
            logger.info(String.format("Receive file to %s", location));
            while(read != -1) {
                byte content[] = new byte[FTPConstants.CHUNK_SIZE];
                read = randomAccess.read(content, 0, FTPConstants.CHUNK_SIZE);
                if(read != -1) {
                    logger.info(String.format("ReceiveByOffset: Read bytes %d", read));
                    size += read;
                    writer.write(VooServerHelper.toCharArray(content), 0, read);
                    writer.flush();
                } else {
                    break;
                }
            }
        } catch(Exception ex) {
            logger.severe(ex.getMessage());
        } finally {
            if (randomAccess != null) {
                try {
                    randomAccess.close();
                } catch(Exception ex) {
                    logger.severe(ex.getMessage());
                }
            }
        }
    }
	
	@Override
	public void run() {
		try {
            logger.info("Start main cycle of data server");
            writer.write(FTPMessages.getMessage("200"));
			while(true) {
				int read = reader.read(buffer, 0, FTPConstants.CHUNK_SIZE);
				if (read != -1) {
					String command = VooServerHelper.toStringCommand(buffer, read);
					logger.info(command);
				}
				else {
					break;
				}
			}
		} catch(Exception ex) {
			logger.severe(ex.getMessage());
		} finally {
			close();
		}
	}
	
	public void close() {
		try {
			if (reader != null) {
				reader.close();
			}
			if (writer != null) {
				writer.close();
			}
			if (socket != null) {
				socket.close();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

    // Return valid location by relative link in the server public directory.
    private String getLocation(String location) {
        return String.format("%s/%s", parent.getLocation(), location);
    }
}
