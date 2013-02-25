package Protocol;

public class TimeOutResponse extends Response {

	public TimeOutResponse(int RequestId)
	{
		super(RequestId);
		// TODO Auto-generated constructor stub
	}
	//
	// Een response voor als er even niet kan geantwoord worden
	// bijvoorbeeld bij een AnswerRequest maar er zijn op dat moment geen onverbeterde vragen
	//
}
