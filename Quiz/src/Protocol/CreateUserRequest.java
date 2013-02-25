package Protocol;

import Protocol.LoginResponse.UserType;

/**
 * een request voor een nieuwe user maken
 * @author vrolijkx
 */
public class CreateUserRequest extends Request {
	private static final long serialVersionUID = -7238453879449333835L;
	private String userName;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private UserType userType;
	
	/**
	 * het antwoord op deze request zal een login response zijn
	 * @param userName
	 * @param firstName
	 * @param lastName
	 * @param email
	 */
	public CreateUserRequest(String userName,String password ,String firstName,
			String lastName, String email,UserType type) {
		super();
		this.userName = userName;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.userType = type;
	}

	public String getUserName() {
		return userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public UserType getUserType() {
		return userType;
	}
	
	public String getPassword() {
		return password;
	}

	@Override
	public Response createResponse() {
		LoginResponse r = new LoginResponse(requestId);
		return r;
	}



}
