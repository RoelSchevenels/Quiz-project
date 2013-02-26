package Protocol.requests;

import Protocol.responses.PictureResponse;
import Protocol.responses.Response;

public class PictureRequest extends Request{
	private static final long serialVersionUID = 1L;
	public int questionId;
	
	
	
	@Override
	public PictureResponse createResponse() {
		return new PictureResponse(requestId);
	}
	
	
}
