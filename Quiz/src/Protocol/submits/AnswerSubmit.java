package Protocol.submits;

public class AnswerSubmit extends Submit {
	private static final long serialVersionUID = -1832729382299066684L;
	private int roundId;
	private int quizId;
	private int questionId;
	private String answer;
	
	
	
	public AnswerSubmit(int roundId, int quizId, int questionId, String answer) {
		this.roundId = roundId;
		this.quizId = quizId;
		this.questionId = questionId;
		this.answer = answer;
	}
	
	public int getRoundId() {
		return roundId;
	}
	public int getQuizId() {
		return quizId;
	}
	public int getQuestionId() {
		return questionId;
	}
	public String getAnswer() {
		return answer;
	}
	
	
	
}
