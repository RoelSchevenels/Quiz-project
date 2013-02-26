package Protocol.submits;

public class RoundSubmit extends Submit {
	private static final long serialVersionUID = -2069556917467963700L;
	private int quizId;
	private int roundId;
	private int amountOfQuestions;
	private String roundName;
	
	
	
	public RoundSubmit(int quizId,int roundId, int amountOfQuestions, String roundName) {
		this.quizId = quizId;
		this.roundId = roundId;
		this.amountOfQuestions = amountOfQuestions;
		this.roundName = roundName;
	}

	public int getRoundId() {
		return roundId;
	}
	
	public int getAmountOfQuestions() {
		return amountOfQuestions;
	}
		
	public String getRoundName() {
		return roundName;
	}

	
	public int getQuizId() {
		return quizId;
	}

	
}
