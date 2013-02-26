package Protocol.requests;

import Protocol.responses.AnswerResponse;

public class AnswerRequest extends Request {
	private static final long serialVersionUID = -2969553828958817338L;
	private int quizId;
	

	@Override
	public AnswerResponse createResponse() {
		AnswerResponse r = new AnswerResponse(requestId);
		r.setQuizId(quizId);
		return r;
	}

	public void setQuizId(int quizId) {
		this.quizId = quizId;
	}

	public int getQuizId() {
		return quizId;
	}


	// gestuurd door de jurry
	//
	// vraag voor een onverbeterd antwoord
	// kan beantwoord worden met een aswerResponse
	// of een TimeOutResponse
	//
}
