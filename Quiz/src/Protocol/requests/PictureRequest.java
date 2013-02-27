package Protocol.requests;

import Protocol.exceptions.IdRangeException;
import Protocol.responses.PictureResponse;
import Protocol.responses.Response;

public class PictureRequest extends Request{
	private static final long serialVersionUID = 1L;
	public int questionId;
	
	public PictureRequest() throws IdRangeException {
		super();
	}
	
	@Override
	public PictureResponse createResponse() {
		return new PictureResponse(requestId);
	}
	
	
}
