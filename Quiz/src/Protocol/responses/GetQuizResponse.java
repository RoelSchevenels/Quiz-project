package Protocol.responses;

public class GetQuizResponse extends Response {
	private static final long serialVersionUID = -3861811974216910872L;
	private int quizId;
	private String quizName;

	public GetQuizResponse(int requestId) {
		super(requestId);
	}

	public int getQuizId() {
		return quizId;
	}

	public void setQuizId(int quizId) {
		this.quizId = quizId;
	}

	public String getQuizName() {
		return quizName;
	}

	public void setQuizName(String quizName) {
		this.quizName = quizName;
	}
	
	
	

}
