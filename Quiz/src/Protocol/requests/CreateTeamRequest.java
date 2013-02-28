package Protocol.requests;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import Protocol.exceptions.IdRangeException;
import Protocol.responses.TeamLoginResponse;

public class CreateTeamRequest extends Request {
	private static final long serialVersionUID = -4262888550652881443L;
	private String teamName;
	private String password;
	private int creatorId;
	private ArrayList<Integer> playerIds = new ArrayList<Integer>();

	public CreateTeamRequest() throws IdRangeException, UnknownHostException, IOException {
		super();
	}
	
	@Override
	public TeamLoginResponse createResponse() {
		return new TeamLoginResponse(requestId);
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ArrayList<Integer> getPlayerIds() {
		return playerIds;
	}

	public void addPlayer(Integer playerID) {
		this.playerIds.add(playerID);
	}

	
	public int getCreatorId() {
		return creatorId;
	}
	

	public void setCreatorId(int creatorId) {
		this.creatorId = creatorId;
	}

}
