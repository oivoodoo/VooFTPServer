import java.util.HashMap;

enum Commands {
	NONE, USER, PASS, ACCT, CWD, CDUP, REIN, QUIT, PORT, PASV, TYPE, STRU, MODE, RETR, STOR, APPE,ALLO,
	REST, RNFR, ABOR, DELE, RMD, MKD, PWD, LIST, NLST, SITE, SYST, STAT, HELP, NOOP
}

public class                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                FTPCommands {
	// Access Control Commands
	public static final String USER = "USER %s\r\n";
	public static final String PASS = "PASS %s\r\n";
	public static final String ACCT = "ACCT %s\r\n";
	// Change directory, directory param.
	public static final String CWD = "CWD %s\r\n";
	public static final String CDUP = "CDUP\r\n";
	public static final String REIN = "REIN\r\n";
	public static final String QUIT = "QUIT\r\n";
	
	// Transfer Parameter Commands
	public static final String PORT = "PORT %s\r\n"; // h1,h2,h3,h4,p1,p2
	public static final String PASV = "PASV\r\n";
	// A - ASCII, N - Non-print, T - telnet,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
	// E - EBCDIC, C - Carriage Control, I - image, 
	// L - Local byte size
	// A, N default types
	public static final String TYPE = "TYPE %s\r\n";
	// File Structure
	// F - File structure, R - Record structure,
	// P - Page structure, 
	// F - default value
	public static final String STRU = "STRU %s\r\n";
	// Transfer mode
	// S - Stream, B - Block, 
	// C - Compressed
	// S - default value
	public static final String MODE = "MODE %s\r\n";
	
	// Service Commands
	// Retrive file, directory param.
	public static final String RETR = "RETR %s\r\n";
	// Store file to ftp server to the current directory.
	// directory param.
	public static final String STOR = "STOR %s\r\n";
	// directory param
	// Store file but if already exists append it.
	// directory param
	public static final String APPE = "APPE %s\r\n";
	// This type of server doesn't impelement ALLOCATE command.
	public static final String ALLO = "ALLO %s\r\n";
	// offset param
	public static final String REST = "REST %s\r\n";
	// Start rename file
	// directory param
	public static final String RNFR = "RNFR %s\r\n";
	// End rename file
	public static final String RNTO = "RNTO %s\r\n";
	// ABORT
	public static final String ABOR = "ABOR\r\n";
	// Remove file
	// directory param
	public static final String DELE = "DELE %s\r\n";
	// Remove directory
	// directory param
	public static final String RMD = "RMD %s\r\n";
	public static final String MKD = "MKD %s\r\n";
	// Print current directory
	public static final String PWD = "PWD\r\n";
	// List of files in directory
	// if directory is empty print list of files in current directory
	// details list 
	public static final String LIST = "LIST %s\r\n";
	// Name list, print simple list of files
	// if directory is emply print list of files in current directory
	public static final String NLST = "NLST %s\r\n";
	public static final String SITE = "SITE %s\r\n";
	// OS information
	public static final String SYST = "SYST\r\n";
	public static final String STAT = "STAT %s\r\n";
	public static final String HELP = "HELP\r\n";
	public static final String NOOP = "NOOP\r\n";
	
	private static HashMap<Commands, String> commands = new HashMap<Commands, String>();
	
	static {
		commands.put(Commands.USER, USER);
		commands.put(Commands.PASS, PASS);
		commands.put(Commands.ACCT, ACCT);
		commands.put(Commands.CWD, CWD);
		commands.put(Commands.CDUP, CDUP);
		commands.put(Commands.REIN, REIN);
		commands.put(Commands.QUIT, QUIT);
		commands.put(Commands.PORT, PORT);
		commands.put(Commands.PASV, PASV);
		commands.put(Commands.TYPE, TYPE);
		commands.put(Commands.STRU, STRU);
		commands.put(Commands.MODE, MODE);
		commands.put(Commands.RETR, RETR);
		commands.put(Commands.STOR, STOR);
		commands.put(Commands.APPE, APPE);
		commands.put(Commands.ALLO, ALLO);
		commands.put(Commands.REST, REST);
		commands.put(Commands.RNFR, RNFR);
		commands.put(Commands.ABOR, ABOR);
		commands.put(Commands.DELE, DELE);
		commands.put(Commands.RMD, RMD);
		commands.put(Commands.MKD, MKD);
		commands.put(Commands.PWD, PWD);
		commands.put(Commands.LIST, LIST);
		commands.put(Commands.NLST, NLST);
		commands.put(Commands.SITE, SITE);
		commands.put(Commands.SYST, SYST);
		commands.put(Commands.STAT, STAT);
		commands.put(Commands.HELP, HELP);
		commands.put(Commands.NOOP, NOOP);
	}
	
	public static Commands parseCommand(String command) {
		for(Commands key : commands.keySet()) {
			if (commands.get(key).contains(command)) {
				return key;
			}
		}
		return Commands.NONE;
	}
}

