package Protocol.submits;

public class AuthSubmit extends Submit{
	private static final long serialVersionUID = -5898023737423437866L;
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
