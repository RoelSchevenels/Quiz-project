package BuisinesLayer.questions;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import BuisinesLayer.QuizMaster;


@Entity
@DiscriminatorValue("NORMAL")
public class StandardQuestion extends Question {

	public StandardQuestion(QuizMaster creator) {
		super(creator);
	}
	
	public StandardQuestion(QuizMaster creator, String question, String answer) {
		this(creator);
		this.setCorrectAnswer(answer);
		this.setQuestion(question);
	}
	
	
	@SuppressWarnings("unused")
	private StandardQuestion() {}; //no argument constructor for hibernate

}
