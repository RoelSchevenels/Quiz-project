package Protocol;

public class LoginRequest extends Request{
	private static final long serialVersionUID = -6885893663339115052L;
	private String userName;
	private String password;
	
	public LoginRequest() {
		super();
	}

	public LoginResponse createResponse() {
		LoginResponse r = new LoginResponse(requestId);
		return r;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
