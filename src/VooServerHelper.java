
public class VooServerHelper {
	public static String toStringCommand(char[] buffer) {
		return toStringCommand(buffer, buffer.length);
	}
	
	public static String toStringCommand(char[] buffer, int read) {
		String result = "";
		for(int i = 0; i < read; i++) {
			result += Character.toString(buffer[i]);
		}
		return result;
	}

    // Create unique id for saving ftp sessions.
	public static String getUniqueId(String hostName, String port) {
		return String.format("%s__%s", hostName, port);
	}
	
	public static char[] toCharArray(byte[] content) {
		char values[] = new char[content.length];
		for(int i = 0; i < content.length; i++) {
			values[i] = (char) content[i];
		}
		return values;
	}
}
