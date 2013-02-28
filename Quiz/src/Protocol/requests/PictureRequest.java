package Protocol.requests;

import java.io.IOException;
import java.net.UnknownHostException;

import Protocol.exceptions.IdRangeException;
import Protocol.responses.PictureResponse;
import Protocol.responses.Response;

public class PictureRequest extends Request{
	private static final long serialVersionUID = 1L;
	public int questionId;
	
	public PictureRequest() throws IdRangeException, UnknownHostException, IOException {
		super();
	}
	
	@Override
	public PictureResponse createResponse() {
		return new PictureResponse(requestId);
	}
	
	
}
