package Protocol;

import java.util.HashMap;

public class GetTeamsResponse extends Response {
	private static final long serialVersionUID = -497557832590297235L;
	private HashMap<Integer,String> teamItems = new HashMap<Integer,String>();
	
	
	public GetTeamsResponse(int RequestId) {
		super(RequestId);
	}
	
	public void addTeamItem(String name, int id) {
		teamItems.put(id, name);
	}
		
	public HashMap<Integer, String> getTeamItems() {
		return teamItems;
	}

}
