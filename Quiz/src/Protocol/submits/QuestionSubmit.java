package Protocol.submits;

public class QuestionSubmit extends Submit {
	private static final long serialVersionUID = -5862326744272002396L;

	public static enum QuestionType {OPEN,MOVIE,MUSIC,MULTIPLECHOISE,PICTURE};
	private int quizId;
	private int questionRoundId;
	private int questionId;
	private QuestionType type;
	private String question;
	private String[] possibilities;
	
	
	public QuestionSubmit(QuestionType type,int quizId, int questionRoundId, int questionId,
			String question) {
		this.quizId = quizId;
		this.questionRoundId = questionRoundId;
		this.questionId = questionId;
		this.question = question;
		this.type = type;
	}
	
	public QuestionSubmit(QuestionType type,int quizId, int questionRoundId, int questionId,
			String question, String[] possibilities) {
		this(type,quizId,questionRoundId,questionId,question);
		
		this.possibilities = possibilities;
	}

	public AnswerSubmit createAnswerSubmit(String answer,int teamID) {
		return new AnswerSubmit(questionRoundId,quizId,questionId,teamID,answer);
	}

	
	public int getQuizId() {
		return quizId;
	}
	

	public int getQuestionRoundId() {
		return questionRoundId;
	}
	

	public int getQuestionId() {
		return questionId;
	}
	

	public String getQuestion() {
		return question;
	}
	

	public String[] getPossibilities() {
		return possibilities;
	}

	
	public void setPossibilities(String[] possibilities) {
		this.possibilities = possibilities;
	}
	
	public QuestionType getType() {
		return type;
	}


	
}
