package Protocol.responses;

import Protocol.submits.QuestionSubmit.QuestionType;

public class AnswerResponse extends Response {
	private static final long serialVersionUID = -616834543470440095L;
	private QuestionType type;
	private int questionID;
	private int quizId;
	private int answerId;
	private int roundId;
	private int maxScore;
	private String question;
	private String correctAnswer;
	private String givenAnswer;
	private String AnswerPerson;
	
	
	public AnswerResponse(int RequestId) {
		super(RequestId);
	}

	public int getQuestionID() {
		return questionID;
	}

	public void setQuestionID(int questionID) {
		this.questionID = questionID;
	}

	public int getQuizId() {
		return quizId;
	}

	public void setQuizId(int quizId) {
		this.quizId = quizId;
	}

	public int getAnswerId() {
		return answerId;
	}

	public void setAnswerId(int roundId) {
		this.answerId = roundId;
	}

	public String getQuestion() {
		return question;
	}


	public void setQuestion(String question) {
		this.question = question;
	}


	public String getCorrectAnswer() {
		return correctAnswer;
	}


	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}


	public String getGivenAnswer() {
		return givenAnswer;
	}


	public void setGivenAnswer(String givenAnswer) {
		this.givenAnswer = givenAnswer;
	}


	public String getAnswerPerson() {
		return AnswerPerson;
	}


	public void setAnswerPerson(String answerPerson) {
		AnswerPerson = answerPerson;
	}


	public QuestionType getType() {
		return type;
	}


	public void setType(QuestionType type) {
		this.type = type;
	}



	public int getRoundId() {
		return roundId;
	}



	public void setRoundId(int roundId) {
		this.roundId = roundId;
	}



	public int getMaxScore() {
		return maxScore;
	}



	public void setMaxScore(int maxScore) {
		this.maxScore = maxScore;
	}

	
	
}
