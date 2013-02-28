package Protocol.responses;
/**
 * @author Roel
 */
public class TestResponse extends Response{
	private String message;

	public TestResponse(int RequestId)
	{
		super(RequestId);
	}
	
	public void setMessage(String message)
	{
		this.message = message;
	}
	public String getMessage()
	{
		return message;
	}
}
