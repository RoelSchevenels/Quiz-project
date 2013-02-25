package Protocol;

public class LoginRequest extends Request{
	private String userName;
	private String passWord;
	
	public LoginRequest() {
		
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
