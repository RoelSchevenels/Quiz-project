/**
 * Een player
 * @author vrolijkx
 */
package BussinesLayer;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;


@Entity
@DiscriminatorValue("PLAYER")
public class Player extends User {
	
	public Player(String userName, String password) {
		super(userName, password);
	}
	
	@SuppressWarnings("unused")
	private Player() {}
	
	@ManyToMany(mappedBy="players")
	private Collection<Team> teams = new ArrayList<Team>();
	
	public Collection<Team> getPlayedTeams() {
		return teams;
	}

	public void AddTeam(Team team) {
		if(!teams.contains(team)) {
			this.teams.add(team);
		}
	}
	

	
}
