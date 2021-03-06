package Protocol.requests;

import java.io.IOException;
import java.net.UnknownHostException;

import Protocol.exceptions.IdRangeException;
import Protocol.responses.GetQuizResponse;

public class GetQuizRequest extends Request {
	private static final long serialVersionUID = -1222660330044304313L;

	public GetQuizRequest() throws IdRangeException, UnknownHostException,
			IOException {
		super();
	}

	@Override
	public GetQuizResponse createResponse() {
		return new GetQuizResponse(requestId);
	}
	
}
