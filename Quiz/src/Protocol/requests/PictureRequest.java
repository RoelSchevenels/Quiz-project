package Protocol.requests;

import java.io.IOException;
import java.net.UnknownHostException;

import Protocol.exceptions.IdRangeException;
import Protocol.responses.PictureResponse;

public class PictureRequest extends Request{
	private static final long serialVersionUID = 1L;
	private int questionId;
	
	public PictureRequest(int questionID) throws IdRangeException, UnknownHostException, IOException {
		super();
		this.questionId = questionID;
	}
	
	@Override
	public PictureResponse createResponse() {
		PictureResponse response = new PictureResponse(requestId,questionId);
		return  response;
	}
	
	public int getQuestionId() {
		return questionId;
	}
	
	
}
