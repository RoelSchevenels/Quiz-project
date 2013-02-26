package Protocol.responses;

import java.util.HashMap;

public class TeamLoginResponse extends Response {
	private static final long serialVersionUID = 703297845978377733L;
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
}
