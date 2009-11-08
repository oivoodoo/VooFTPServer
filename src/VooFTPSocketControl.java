import java.io.File;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class VooFTPSocketControl extends VooFTPSocketAbstract {
	private String currentUser;
	private String currentPassword;
	private VooUser user = null;
	private String location;
    private Timer timer;
	
	public VooFTPSocketControl(VooFTPServer parent, Socket socket) throws Exception {
		super(parent, socket);
        startTimer();
	}

    private void startTimer() {
        timer = new Timer();
        logger.info("Start timeout timer.");
        timer.schedule(new SocketControlTimerTask(), FTPConstants.TIMEOUT);
    }

    private void restartTimer() {
        logger.info("Cancel timeout timer.");
        timer.cancel();
        timer = new Timer();
        logger.info("Start timeout timer.");
        timer.schedule(new SocketControlTimerTask(), FTPConstants.TIMEOUT);
    }

	@Override
	public void run() {
		try {
            logger.info("Start main cycle of control server");
            send(FTPMessages.getMessage("200"));
			boolean flag = true;
			while(flag) {
				int read = reader.read(buffer, 0, FTPConstants.CHUNK_SIZE);
                restartTimer();
				if (read != -1) {
					String line = VooServerHelper.toStringCommand(buffer, read);
                    String[] commands = line.split("\r\n");
                    for(String value : commands) {
                        String command = value + "\r\n";
                        logger.info(String.format("Command: %s", command));
                        switch(FTPCommands.parseCommand(getCommand(command))) {
                            case USER:
                                userCommand(command);
                                break;
                            case PASS:
                                passCommand(command);
                                break;
                            case RETR:
                                retrCommand(command);
                                break;
                            case STOR:
                                storCommand(command);
                                break;
                            case REST:
                                restCommand(command);
                                break;
                            case HELP:
                                helpCommand(command);
                                break;
                            case PORT:
                                portCommand(command);
                                break;
                            case DELE:
                                deleCommand(command);
                                break;
                            case RMD:
                                rmdCommand(command);
                                break;
                            case MKD:
                                mkdCommand(command);
                                break;
                            case PWD:
                                pwdCommand(command);
                                break;
                            case SYST:
                                systCommand(command);
                                break;
                            case CDUP:
                                cdupCommand(command);
                                break;
                            case CWD:
                                cwdCommand(command);
                                break;
                            case ACCT:
                                send(FTPMessages.getMessage("532")); // not implemented
                                break;
                            case QUIT:
                                flag = false;
                                break;
                            default:
                                send(FTPMessages.getMessage("500"));
                                break;
                        }
                        writer.flush();
                    }
                } else {
                    break;
                }
			}
		} catch(Exception ex) {
            try {
			    send(FTPMessages.getMessage("221"));
            } catch(Exception innerEx) {
			    logger.severe(innerEx.getMessage());
            }
            logger.severe(ex.getMessage());
		} finally {
			try {
				if (reader != null) {
					logger.info("close reader");
					reader.close();
				}
				if (writer != null) {
					logger.info("close writer");
					writer.close();
				}
				if (socket != null) {
					logger.info("close socket");
					socket.close();
				}
			} catch(Exception e) {
                logger.info(e.getMessage());
				e.printStackTrace();
			}
		}
	}

    private void cdupCommand(String command) throws Exception {
        if (isLogin()) {
            parent.getSessions().get(id).upLocation();
            send(FTPMessages.getMessage("200"));
        } else {
            logger.info("Client try to cdup directory without any auth information.");
			send(FTPMessages.getMessage("332"));
        }
    }

    private void cwdCommand(String command) throws Exception {
        if (isLogin()) {
            try {
                parent.getSessions().get(id).intoLocation(getValue(command));
            } catch(Exception ex) {
                send(String.format(FTPMessages.SERVER_MESSAGE, ex.getMessage()));
                return;
            }
            send(FTPMessages.getMessage("200"));
        } else {
            logger.info("Client try to cdup directory without any auth information.");
            send(FTPMessages.getMessage("332"));
        }
    }

    private void systCommand(String command) throws Exception {
        if (isLogin()) {
            Runtime time = Runtime.getRuntime();
            send(String.format(FTPMessages.SERVER_MESSAGE,
                String.format("Processors: %d; Memory: %d", time.availableProcessors(), time.totalMemory())));
        } else {
            logger.info("Client try to make directory without any auth information. (File: %s)");
			send(FTPMessages.getMessage("332"));
        }
    }

    private void pwdCommand(String command) throws Exception {
        if(isLogin()) {
            // Doesn't support commands such as cd and other that's works with entries folders.
            send(String.format(FTPMessages.SERVER_MESSAGE, parent.getSessions().get(id).getLocation()));
        } else {
            logger.info(String.format("Client try to make directory without any auth information. (File: %s)", getValue(command)));
			send(FTPMessages.getMessage("332"));
		}
    }

    private void mkdCommand(String command) throws Exception {
        if(isLogin()) {
            logger.info(String.format("Make folder %s", getValue(command)));
            parent.getSessions().get(id).makeFolder(getValue(command));
            send(FTPMessages.getMessage("200"));
        } else {
            logger.info(String.format("Client try to make directory without any auth information. (File: %s)", getValue(command)));
			send(FTPMessages.getMessage("332"));
		}
    }

    private void rmdCommand(String command) throws Exception {
        if(isLogin()) {
            logger.info(String.format("Remove folder %s", getValue(command)));
            parent.getSessions().get(id).deleteFolder(getValue(command));
            send(FTPMessages.getMessage("200"));
        } else {
            logger.info(String.format("Client try to retrieve file without any auth information. (File: %s)", getValue(command)));
			send(FTPMessages.getMessage("332"));
		}
    }
	
	private int login(VooUser user) {
        logger.info(String.format("Login user %s - %s", user.getUserName(), user.getPassword()));
		return parent.isValidUser(user) == false ? -1 : 0; 
	}
	
	private void portCommand(String command) throws Exception {
    		if (isLogin()) {
            // create connect to remote client for sending data.
            // command value contains next values h1,h2,h3,h4,p1,p2
            String value = getValue(command);
            String[] datas = value.split(",");
            String host = String.format("%s.%s.%s.%s", datas[0], datas[1], datas[2], datas[3]);
            int port = 256 * Integer.parseInt(datas[4]) + Integer.parseInt(datas[5]);
            logger.info(String.format("Try to create connection to client via %s:%d", host, port));
            // before connect need send to client ftp message.
            Socket socket = new Socket(host, port);
            parent.getSessions().get(id).setDataSocket(new VooFTPSocketData(parent, socket));
            logger.info(String.format("Connection is ok(%s:%d)", host, port));
			send(FTPMessages.getMessage("200"));
		} else {
            logger.info(String.format("Client try to retrieve file without any auth information. (File: %s)", getValue(command)));
			send(FTPMessages.getMessage("332"));
		}
	}
	
	private void helpCommand(String command) throws Exception {
        logger.info("Run help command");
		send(String.format(FTPMessages.SERVER_MESSAGE, "THIS IS VOODOO FTP SERVER"));
	}
	
	private void passCommand(String command) throws Exception {
		currentPassword = getValue(command);
		VooUser user = new VooUser(currentUser, currentPassword);
		if (login(user) == -1) {
			// answer that you haven't rights to this server.
			logger.info("You haven't rights to this ftp server");
			currentUser = currentPassword = "";
			send(FTPMessages.getMessage("530"));
		} else {
			send(String.format(FTPMessages.getMessage("230"), currentUser));
            logger.info(String.format("Save user - %s, %s", user.getUserName(), user.getPassword()));
			this.user = user;
		}
	}
	
	private void userCommand(String command) throws Exception {
		currentUser = getValue(command);
        logger.info(String.format("UserName is %s", currentUser));
		send(FTPMessages.getMessage("331"));
	}
	
	private boolean isLogin() {
        logger.info(String.format("Try to login with %s, %s user auth params", user.getUserName(), user.getPassword()));
		return user != null && user.Valid();
	}
	
	private void retrCommand(String command) throws Exception {
		if (isLogin()) {
			location = getValue(command);
            logger.info(String.format("RETR: Location %s", location));
            String fileDirectory = parent.getSessions().get(id).getLocation() + "/" + location;
			File file = new File(fileDirectory);
			if (file.exists()) {
                logger.info(String.format("RETR: %s exists", fileDirectory));
				send(FTPMessages.getMessage("150"));
				parent.getSessions().get(id).getDataSocket().send(fileDirectory);
                parent.getSessions().get(id).getDataSocket().close();
                send(FTPMessages.getMessage("226"));
			} else {
                logger.info("File doesn't exists");
				send(FTPMessages.getMessage("332"));
			}
		} else {
            logger.info("You doesn't login yet.");
			send(FTPMessages.getMessage("332"));
		}
	}
	
	private void storCommand(String command) throws Exception {
		if (isLogin()) {
            logger.info(String.format("STOR: Location %s", getValue(command)));
            send(FTPMessages.getMessage("150"));
			parent.getSessions().get(id).getDataSocket().receive(getValue(command));
            send(FTPMessages.getMessage("200"));
		} else {
            logger.info(String.format("Client try to retrieve file without any auth information. (File: %s)", getValue(command)));
			send(FTPMessages.getMessage("532"));
        }
	}
	
	private void restCommand(String command) throws Exception {
		if (isLogin()) {
            logger.info(String.format("REST: offset is %s", getValue(command)));
	        parent.getSessions().get(id).getDataSocket().receiveByOffset(Integer.parseInt(getValue(command)));
            send(FTPMessages.getMessage("200"));
		} else {
            logger.info(String.format("Client try to retrieve by offset file without any auth information. (File: %s)", getValue(command)));
			send(FTPMessages.getMessage("332"));
		}
	}

    private void deleCommand(String command) throws Exception {
        if(isLogin()) {
            String filename = getValue(command);
            logger.info(String.format("Try to delete file %s", filename));
            parent.getSessions().get(id).delete(filename);
            send(FTPMessages.getMessage("200"));
        } else {
            logger.info(String.format("Client try to retrieve by offset file without any auth information. (File: %s)", getValue(command)));
			send(FTPMessages.getMessage("332"));
		}
    }

	public void close() {
		try {
			if (reader != null) {
				logger.info("close reader");
				reader.close();
			}
			if (writer != null) {
				logger.info("close writer");
				writer.close();
			}
			if (socket != null) {
				logger.info("close socket");
				socket.close();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

    private void clearSession() {
        try {                  // it's bone
            VooFTPSocketData dataSocket = parent.getSessions().get(id).getDataSocket();
            if (dataSocket != null) {
                dataSocket.close();
            }
        } catch(Exception ex) {
        }
        parent.getSessions().remove(id);
    }

    class SocketControlTimerTask extends TimerTask {
        @Override
        public void run() {
            clearSession();
            close();
        }
    }
} 
