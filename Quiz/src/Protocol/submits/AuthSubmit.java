package Protocol.submits;

public class AuthSubmit extends Submit{
	private String role;
	
	public AuthSubmit(String role)
	{
		this.role = role;
	}
	public String getRole()
	{
		return role;
	}

	public void setRole(String role)
	{
		this.role = role;
	}
}
