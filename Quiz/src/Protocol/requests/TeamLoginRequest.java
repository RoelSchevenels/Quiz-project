package Protocol.requests;

import Protocol.exceptions.IdRangeException;
import Protocol.responses.TeamLoginResponse;

public class TeamLoginRequest extends Request {
	private static final long serialVersionUID = -6748642844107959371L;
	private final int teamId;
	private final String password;
	
	public TeamLoginRequest(int teamId, String password) throws IdRangeException {
		super();
		this.teamId =teamId;
		this.password = password;
	}
	
	@Override
	public TeamLoginResponse createResponse() {
		return new TeamLoginResponse(requestId);
	}

	public int getTeamId() {
		return teamId;
	}

	public String getPassword() {
		return password;
	}

}
