/**
 * een simpele vragen ronde die in meerdere quises kan gebruikt worden
 * @author vrolijkx
 */
package BussinesLayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import BussinesLayer.questions.Question;

@Entity
@Table(name="ROUND")
public class QuestionRound {
	@Id
	@GeneratedValue
	private int id;
	@Column(length=50,name="ROUND_NAME")
	private String name;
	
	@ManyToMany(mappedBy="rounds")
	private Collection<Quiz> quises = new ArrayList<Quiz>();
	@ManyToMany(mappedBy="questionRounds")
	private List<Question> questions = new ArrayList<Question>();
	
	public QuestionRound(String roundName) {
		this.setName(roundName);
	}
	
	@SuppressWarnings("unused")
	private QuestionRound() {}; //constructor voor hibernate
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Collection<Quiz> getQuises() {
		return quises;
	}
	
	public void addQuiz(Quiz q) {
		if(!quises.contains(q)) {
			quises.add(q);
		}
	}
	
	public void removeQuiz(Quiz q) {
		if(quises.contains(q)) {
			quises.remove(q);
		}
	}
	
	public List<Question> getQuestions() {
		return questions;
	}

	public void addQuestion(Question q) {
		if(!questions.contains(q)) {
			questions.add(q);
		}
	}
	
	public void removeQuestion(Question q) {
		if(questions.contains(q)) {
			questions.remove(q);
		}
	}
	
	public int getRoundId() {
		return this.id;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	
}
