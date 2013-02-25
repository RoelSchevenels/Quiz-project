package BussinesLayer;

import java.util.Date;

import javax.persistence.*;

import BussinesLayer.questions.Question;

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
	private Quiz quiz;
	@ManyToOne 
	private QuestionRound round;
	@ManyToOne
	private Team team;	
	@ManyToOne
	private Jury jury;	
	@Column(name="SCORE")
	private int score;	
	@Column(name="DATE") 
	private Date antwoordDatum;
	
	
	public Answer(Question q,Team t,Quiz quiz, QuestionRound round) {
		this.quiz = quiz;
		this.round = round;
		antwoordDatum = new Date();
		this.team = t;
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
	
	
	public Quiz getQuiz() {
		return quiz;
	}

	public QuestionRound getRound() {
		return round;
	}
	
	public Question getQuestion() {
		return this.question;
	}
	
	public Team getTeam() {
		return team;
	}

	private void setJury(Jury j) {
		if(this.jury != j) {
			this.jury=j;
			jury.addAnswer(this);
		}
	}
	
	public Jury getJury() {
		return this.jury;
	}
	
	public void setAnwer(String answer) {
		this.answer = answer;
	}
	
	public String getAnswer() {
		return this.answer;
	}
	
	public int getAnswerId() {
		return this.answerId;
	}
	
}
