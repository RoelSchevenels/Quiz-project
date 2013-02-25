package Protocol;

public class GetTeamsRequest extends Request {
	private static final long serialVersionUID = 6588272773200109384L;
	private int userId;
	
	public GetTeamsRequest(int userId) {
		super();
		this.userId = userId;
	}

	@Override
	public GetTeamsResponse createResponse() {
		GetTeamsResponse s = new GetTeamsResponse(requestId);
		return s;
	}

	public int getUserId() {
		return userId;
	}

}
