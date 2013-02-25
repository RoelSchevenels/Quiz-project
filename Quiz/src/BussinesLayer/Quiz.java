/**
 * @author vrolijkx
 * Het quizobject
 */
package BussinesLayer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="QUIZ")
public class Quiz {
	@Id
	@GeneratedValue
	@Column(name="QUIZ_ID")
	private int quizID;
	
	@Column(name="QUIZ_NAME",nullable=false)
	private String quizName;
	
	@Column(name="MIN_TEAMS")
	private int minTeams;
	
	@Column(name="MAX_TEAMS")
	private int maxTeams;
	
	@Column(name="CREATION_DATE")
	private Date creationDate;
	
	@Column(name="LOCATION",length=85) //lengte langste stadsnaam ter wereld (volgens wiki)
	private String location;
	
	@Column(name="DISCRIPTION",length=250)
	private String beschrijving;
	
	//de links met ander objecten
	@ManyToOne
	private QuizMaster creator;
	
	@ManyToMany
	@JoinTable(
			name = "QUIZ_ROUND",
			joinColumns = {@JoinColumn(name="QUIZ_ID")},
			inverseJoinColumns = {@JoinColumn(name="ROUND_ID")}
			)
	private List<QuestionRound> rounds = new ArrayList<QuestionRound>();
	
	@ManyToMany
	@JoinTable(
		name = "TEAM_QUIZ",
		joinColumns = {@JoinColumn(name = "QUIZ_ID")},
		inverseJoinColumns = {@JoinColumn(name = "TEAM_ID")}
	)
	private List<Team> teams = new ArrayList<Team>();
	
	
	
	public Quiz(String quizName, QuizMaster creator) {
		this.quizName = quizName;
		this.creator = creator;
		minTeams = 2;
		maxTeams = 5;
	}
	
	/**
	 * standaard consturctor voor hibernate
	 */
	@SuppressWarnings("unused")
	private Quiz() {}
	
 	public String getQuizName() {
		return quizName;
	}

	public void setQuizName(String quizName) {
		this.quizName = quizName;
	}


	public String getBeschrijving() {
		return beschrijving;
	}

	public void setBeschrijving(String beschrijving) {
		this.beschrijving = beschrijving;
	}

	public int getMinTeams() {
		return minTeams;
	}

	public void setMinTeams(int minTeams) {
		this.minTeams = minTeams;
	}

	public int getMaxTeams() {
		return maxTeams;
	}

	public void setMaxTeams(int maxTeams) {
		this.maxTeams = maxTeams;
	}

	public QuizMaster getCreator() {
		return creator;
	}

	public void setCreator(QuizMaster creator) {
		this.creator = creator;
	}

	public int getQuizID() {
		return quizID;
	}
	
	public void addTeam(Team t) {
		if(teams.size() < maxTeams && !teams.contains(t)) {
			teams.add(t);
		}
	}
	
	public void removeTeam(Team t) {
		if(teams.contains(t)) {
			teams.remove(t);
		}
	}
	
	public List<Team> getTeams() {
		return teams;
	}

	public void addRound(QuestionRound round) {
		if(!rounds.contains(round)) {
			rounds.add(round);
			round.addQuiz(this);
		}
	}
	
	public void removeRound(QuestionRound round) {
		if(rounds.contains(round)) {
			rounds.remove(round);
			round.removeQuiz(this);
		}
	}

	public List<QuestionRound> getRounds() {
		return rounds;
	}
}
