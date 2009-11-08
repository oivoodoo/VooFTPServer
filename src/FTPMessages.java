import java.util.Hashtable;


public class FTPMessages {
	// 234 Directory is empty\r\n
	public static final String SERVER_MESSAGE = "%s\r\n";
	private static Hashtable<String, String> messages = new Hashtable<String, String>(); 
	
	static {
		messages.put("1yz", "1yz Positive Preliminary\r\n");
		messages.put("2yz", "2yz Positive Completion\r\n");
		messages.put("3yz", "3yz Positive Intermediate Reply\r\n");
		messages.put("4yz", "4yz Transient Negative Completion\r\n");
		messages.put("5yz", "5yz Permanent Negative Completion\r\n");
		messages.put("x0z", "x0z Syntax\r\n");
		messages.put("x1z", "x1z Information\r\n");
		messages.put("x2z", "x2z Connections\r\n");
		messages.put("x3z", "x3z Authentication and Accounting\r\n");
		messages.put("x4z", "x4z Unspecified As Yet\r\n");
		messages.put("110", "110 Restart Marker Reply\r\n");
		messages.put("120", "120 Service Ready in %s minutes\r\n");
		messages.put("125", "125 Data Connection Already Open. Transfer Starting\r\n");
		messages.put("150", "150 File Status Okay. About to Open Data Connection (%%s bytes)\r\n");
		messages.put("200", "200 Command Okay\r\n");
		messages.put("202", "202 Command Not Implemented Superfluous At This Site\r\n");
		messages.put("211", "211 System Status, or System Help Reply\r\n");
		messages.put("212", "212 Directory Status\r\n");
		messages.put("213", "213 File Status\r\n");
		messages.put("214", "214 Help Message\r\n");
		messages.put("215", "215 NAME System Type\r\n");
		messages.put("220", "220 Service Ready For New User\r\n");
		messages.put("221", "221 Service closing control connection\r\n");
		messages.put("225", "225 Data connection open\r\n");
		messages.put("226", "226 Closing data connection\r\n");
		messages.put("227", "227 Entering Passive Mode\r\n");
		messages.put("230", "230 User %%s Logged In, Proceed\r\n");
		messages.put("250", "250 Requested File Action Okay, Completed\r\n");
		messages.put("257", "257 \"%s\" Created\r\n");
		messages.put("331", "331 User Name Okay, Need Password\r\n");
		messages.put("332", "332 Need Account for Login\r\n");
		messages.put("350", "350 Requested File Action Pending Further Information\r\n");
		messages.put("421", "421 Service not Available  Closing Control Connection\r\n");
		messages.put("425", "425 Can't Open Data Connection\r\n");
		messages.put("426", "426 Connection Closed; Transfer Aborted\r\n");
		messages.put("450", "450 Requested File Action not Taken\r\n");
		messages.put("451", "451 Requested Action Aborted. Local Error In Processing\r\n");
		messages.put("452", "452 Requested Action not Taken. Insufficient Storage Space in System\r\n");
		messages.put("500", "500 Syntax Error, Command Unrecognized\r\n");
		messages.put("501", "501 Syntax Error in Parameters or Arguments\r\n");
		messages.put("502", "502 Command not Implemented\r\n");
		messages.put("503", "503 Bad Sequence of Commands\r\n");
		messages.put("504", "504 Command not Implemented for That Parameter\r\n");
		messages.put("530", "530 Not Logged In\r\n");
		messages.put("532", "532 Need Account for Storing Files\r\n");
		messages.put("550", "550 Requested Action not Taken. File Unavailable\r\n");
		messages.put("551", "551 Requested Action Aborted: Page Type Unknown\r\n");
		messages.put("552", "552 Requested File Action  Aborted. Exceeded Storage Allocation\r\n");
		messages.put("553", "553 Requested Action Not Taken. File Name not Allowed\r\n");
	}
	
	public static String getMessage(String name, Object... objects) {
		String message = "";
		if (messages.containsKey(name)) {
			message = String.format(messages.get(name), objects);
		}
		return message;
	}
}
