package Protocol.submits;

public class CorrectSubmit extends Submit {
	private static final long serialVersionUID = -2491301599233302387L;
	private int juryId;
	private int AnswerId;
	private int score;
	
	public CorrectSubmit(int AnswerId,int juryId, int score) {
		this.juryId = juryId;
		this.AnswerId = AnswerId;
		this.score = score;
	}

	public int getJuryId() {
		return juryId;
	}

	public int getAnswerId() {
		return AnswerId;
	}

	public int getScore() {
		return score;
	}	
	
}
