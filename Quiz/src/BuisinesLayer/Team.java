package BuisinesLayer;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import Util.Security;

@Entity
public class Team {
	@Id
	@GeneratedValue
	@Column(name="TEAM_ID")
	private int teamId;
	
	@Column(length=50,nullable=false,name="TEAM_NAME")
	private String teamName;
	
	@Column(name="SALT",length=80)
	private String salt;
	@Column(name="PASS",length=50)
	private String password;
	
	
	//De tussentabbel aanmaken voor manny to manny assosiatie.
	@ManyToMany
	@JoinTable(
		name = "TEAM_PLAYER",
		joinColumns = {@JoinColumn(name = "TEAM_ID")},
		inverseJoinColumns = {@JoinColumn(name = "USER_ID")}
	)
	private List<Player> players = new ArrayList<Player>();
	
	@OneToMany(mappedBy="team")
	private List<Answer> answers = new ArrayList<Answer>();
	
	
	public Team(String teamname,String password) {
		this.setTeamName(teamname);
		this.salt = Security.getRandomSalt();
		this.password = Security.encrypt(password, salt);
	}
	
	/**
	 * de consturctor zonder argumenten die nodig is voor hibernate
	 * 
	 */
	@SuppressWarnings("unused")
	private Team() {}
	
 	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String ploegNaam) {
		this.teamName = ploegNaam;
	}

	public int getTeamId() {
		return teamId;
	}

	public List<Player> getPlayers() {
		return  players;
	}
	
	public void addPlayer(Player p) {
		if(!players.contains(p)) {
			players.add(p);
			p.AddTeam(this);
		}
	}
	
	public void removerPlayer(Player p) {
		if(players.contains(p)) {
			players.remove(p);
			p.AddTeam(this);
		}
	}

	public void removeAllPlayers() {
		players.clear();
	}
	
	public Boolean hassPassword(String pass) {
		return Security.checkPassword(pass, salt, this.password);
		
	}
}
