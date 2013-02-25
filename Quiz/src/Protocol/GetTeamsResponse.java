package Protocol;

import java.util.ArrayList;

public class GetTeamsResponse extends Response {
	private static final long serialVersionUID = -497557832590297235L;
	private ArrayList<TeamItem> teamItems = new ArrayList<TeamItem>();
	

	public GetTeamsResponse(int RequestId) {
		super(RequestId);
	}
	
	public void addTeamItem(String name, int id) {
		
	}
		
	public ArrayList<TeamItem> getTeamItems() {
		return teamItems;
	}

	public class TeamItem {
		private int id;
		private String name;
		
		public TeamItem(int id, String name) {
			this.id = id;
			this.name = name;
		}
		
		public int getId() {
			return id;
		}

		public String getName() {
			return name;
		}
	}
}
