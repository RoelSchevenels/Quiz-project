package Protocol;

public class LoginRequest extends Request{
	private static final long serialVersionUID = -6885893663339115052L;
	private String userName;
	private String password;
	
	public LoginRequest(String userName,String password) {
		this.userName = userName;
		this.password = password;
		
	}

	public LoginResponse createResponse() {
		LoginResponse r = new LoginResponse(requestId);
		return r;
	}
	
	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

}
