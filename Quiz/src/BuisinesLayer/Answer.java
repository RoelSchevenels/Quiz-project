package BuisinesLayer;

import java.util.Date;

import javax.persistence.*;

import BuisinesLayer.questions.Question;

@Entity
@Table(name="ANSWER")
public class Answer {
	@Id
	@GeneratedValue
	@Column(name="ID")
	private int answerId;
	@Column(name="GIVEN_ANSWER")
	private String answer;
	@ManyToOne(fetch=FetchType.EAGER)
	private Question question;	
	@ManyToOne
	private Team team;	
	@ManyToOne
	private Jury jury;	
	@Column(name="SCORE")
	private int score;	
	@Column(name="DATE") 
	private Date antwoordDatum;
	
	
	public Answer(Question q,Team t,String answer) {
		antwoordDatum = new Date();
		this.team = t;
		this.answer = answer;
	}
	
	@SuppressWarnings("unused")
	private Answer() {}; //hibernate constructor

	public void correct(Jury j,int score) {
		this.setJury(j);
		if(score <= this.getMaxScore()) {
			this.score = score;
		}
		
	}

	public int getMaxScore() {
		return question.getMaxScore();
	}
	
	private void setJury(Jury j) {
		if(this.jury != j) {
			this.jury=j;
			jury.addAnswer(this);
		}
		
	}
	
}
