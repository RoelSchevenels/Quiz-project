package Protocol.submits;

public class CorrectSubmit extends Submit {
	private static final long serialVersionUID = -2491301599233302387L;
	private int jurryId;
	private int AnswerId;
	private int score;
	
	public CorrectSubmit(int AnswerId,int jurryId, int score) {
		this.jurryId = jurryId;
		this.AnswerId = AnswerId;
		this.score = score;
	}

	public int getJurryId() {
		return jurryId;
	}

	public int getAnswerId() {
		return AnswerId;
	}

	public int getScore() {
		return score;
	}	
	
}
