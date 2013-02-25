/**
 * @author vrolijkx
 * 
 */
package BussinesLayer.questions;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import BussinesLayer.QuestionRound;
import BussinesLayer.QuizMaster;


@Entity
@Table(name="QUESTION")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
		name="TYPE",
		discriminatorType=DiscriminatorType.STRING
			)
public abstract class Question {
	//de id voor in de database
	@Id
	@GeneratedValue
	@Column(name="QUESTION_ID")
	private int questionId;

	@Lob
	@Column(name="QUESTION",nullable=false,columnDefinition="CLOB")
	private String question;

	@Column(length=150,name="ANSWER",nullable=false)
	private String correctAnswer;
	
	@Column(name="MAX_SCORE")
	private int maxScore;
	
	@ManyToOne
	private QuizMaster creator;
	
	@ManyToMany
	@JoinTable(
			name="QUESTION_ROUND",
			joinColumns=@JoinColumn(name="QUESTION"),
			inverseJoinColumns=@JoinColumn(name="ROUND")
			)
	private Collection<QuestionRound> questionRounds = new ArrayList<QuestionRound>();
	
	//FXME: zorgen dat question en answer moet worden meegegeven zie ook subClasses
	public Question(QuizMaster creator) {
		this.creator = creator;
		this.maxScore = 10;
	}
 	
	/*
	 * Empty constructor for hibernate
	 */
	public Question() {};
 	
 	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getCorrectAnswer() {
		return correctAnswer;
	}

	public void setCorrectAnswer(String answer) {
		this.correctAnswer = answer;
	}

	public int getQuestionId() {
		return questionId;
	}

	public QuizMaster getCreator() {
		return creator;
	}

	public Collection<QuestionRound> getQuestionRounds() {
		return questionRounds;
	}
	
	public void addQuestionRound(QuestionRound round) {
		if(!questionRounds.contains(round)) {
			questionRounds.add(round);
			round.addQuestion(this);
		}
	}
	
	public void removeQuestionRound(QuestionRound round) {
		if(questionRounds.contains(round)) {
			questionRounds.remove(round);
			round.removeQuestion(this);
		}
	}
	
 	public int getMaxScore() {
		return maxScore;
	}

	public void setMaxScore(int maxScore) {
		this.maxScore = maxScore;
	}

	/**
	 * statiche methode die door iedere subklassen overschreven
	 * moet worden.
	 * @return string met de beschrijving
	 */
 	public static String getDiscription() {
        throw new IllegalStateException("Deze methode moet door subclasses overschreven worden");
    }
}
