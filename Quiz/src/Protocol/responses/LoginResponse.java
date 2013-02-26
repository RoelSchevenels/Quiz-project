package Protocol.responses;

import network.Server;

public class LoginResponse extends Response{
	private static final long serialVersionUID = -67840159000689435L;
	public static enum UserType {JURRY,PLAYER}
	private Integer userId;
	private UserType userType;
	private String userName;
	private String firstname;
	private String lastName;
	private String email;

	public LoginResponse(int requestId) {
		super(requestId);
	}
	
	@Override
	public void send() {
		if(userType.equals(UserType.JURRY)) {
			Server.getInstance().markRequestAsJurry(requestId);
		}else if(userType.equals(UserType.PLAYER)) {
			Server.getInstance().markRequestAsPlayer(requestId);
		}
		super.send();
	}
		
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
