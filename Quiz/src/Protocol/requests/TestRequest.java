package Protocol.requests;

import java.io.IOException;
import java.net.UnknownHostException;

import Protocol.exceptions.IdRangeException;
import Protocol.responses.TestResponse;
/**
 * @author Roel
 */
public class TestRequest extends Request{
	private static final long serialVersionUID = 4131672112215563556L;
	private String message;

	public TestRequest(String message) throws IdRangeException, UnknownHostException,IOException
	{
		super();
		this.message = message;
	}
	
	public String getMessage()
	{
		return message;
	}

	@Override
	public TestResponse createResponse()
	{
		return new TestResponse(requestId);
	}

}
