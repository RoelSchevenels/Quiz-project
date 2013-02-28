package Protocol.requests;

import java.io.IOException;
import java.net.UnknownHostException;

import Protocol.exceptions.IdRangeException;
import Protocol.responses.PictureResponse;
import Protocol.responses.Response;

public class PictureRequest extends Request{
	private static final long serialVersionUID = 1L;
	public int questionId;
	
<<<<<<< HEAD
	public PictureRequest() throws IdRangeException, UnknownHostException, IOException {
=======
	public PictureRequest() throws IdRangeException, IOException {
>>>>>>> 51f795e14b46c2214a03dd5315b44996fcfbaa10
		super();
	}
	
	@Override
	public PictureResponse createResponse() {
		return new PictureResponse(requestId);
	}
	
	
}
