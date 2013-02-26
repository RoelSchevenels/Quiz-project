package Protocol.responses;

import java.util.HashMap;

public class TeamLoginResponse extends Response {
	private static final long serialVersionUID = 703297845978377733L;
	private int teamId;
	private int creatorId;
	private HashMap<Integer, String> players = new HashMap<Integer,String>();
	
	public TeamLoginResponse(int RequestId) {
		super(RequestId);
	}
	
	public void addPlayer(int playerId,String Username) {
		players.put(playerId, Username);
	}
	
	/**
	 * 
	 * @return een map met van iedere player 
	 * 	de id gekopelt aan zijn username;
	 */
	public HashMap<Integer, String> getPlayer() {
		return players;
	}

	public int getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(int creatorId) {
		this.creatorId = creatorId;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}
}
