package Protocol.requests;

import java.io.IOException;
import java.net.UnknownHostException;

import Protocol.exceptions.IdRangeException;
import Protocol.responses.GetQuizResponse;
import Protocol.responses.Response;

public class GetQuizRequest extends Request {

	public GetQuizRequest() throws IdRangeException, UnknownHostException,
			IOException {
		super();
	}

	@Override
	public Response createResponse() {
		return new GetQuizResponse(requestId);
	}
	
}
