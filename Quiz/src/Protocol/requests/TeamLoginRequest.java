package Protocol.requests;

import Protocol.exceptions.IdRangeException;
import Protocol.responses.TeamLoginResponse;

public class TeamLoginRequest extends Request {
	private static final long serialVersionUID = -6748642844107959371L;
	private int TeamId;
	private String password;
	
	public TeamLoginRequest() throws IdRangeException {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public TeamLoginResponse createResponse() {
		return new TeamLoginResponse(requestId);
	}

	public int getTeamId() {
		return TeamId;
	}

	public void setTeamId(int teamId) {
		TeamId = teamId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
