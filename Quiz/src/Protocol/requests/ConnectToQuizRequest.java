package Protocol.requests;

import java.io.IOException;
import java.net.UnknownHostException;

import Protocol.exceptions.IdRangeException;
import Protocol.responses.Response;

public class ConnectToQuizRequest extends Request {
	private static final long serialVersionUID = 6408538424762947486L;
	private int teamId;
	private int quizId;

	public ConnectToQuizRequest(int teamId,int quizId) throws IdRangeException,
			UnknownHostException, IOException {
		super();
		this.teamId = teamId;
		this.quizId = quizId;
	}

	@Override
	public Response createResponse() {
		return new SuccesResponse(requestId);
	}
	
	public int getTeamId() {
		return teamId;
	}

	public int getQuizId() {
		return quizId;
	}
}
