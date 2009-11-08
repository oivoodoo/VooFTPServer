
public class Main {

    /// @params args[0] contains ftp control port
    /// @params args[1] contains ftp data port
	public static void main(String[] args) {
        VooFTPServer server;
        if (args.length > 1) {
		    server = new VooFTPServer("/home/oivoodoo/labs", Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        } else {
            server = new VooFTPServer("/home/oivoodoo/labs");
        }
		server.start();
	}
}