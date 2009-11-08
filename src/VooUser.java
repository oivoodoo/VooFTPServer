public class VooUser implements Comparable<VooUser> {
	private String userName;
	private String password;
	
	public VooUser(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public String getPassword() {
		return password;
	}
	
	public boolean Valid() { 
		return userName != null && userName != "" &&
			   password != null && password != "";
	}

	@Override
	public int compareTo(VooUser o) {
		if (userName.compareTo(o.getUserName()) == 0 &&
			password.compareTo(o.getPassword()) == 0) {
			return 0;
		}
		return -1;
	}
}
