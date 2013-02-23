/**
 * een mogelijkheid die vasthangt aan een multiplechoisevraag
 * @author vrolijkx
 */
package BuisinesLayer.questions;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="MULTIPLECHOICE")
public class MultipleChoisePosebility {
	@Id
	@GeneratedValue
	@Column(name="ID")
	private int id;
	
	@ManyToOne
	@JoinColumn(name="QUESTION_ID")
	private Question question;
	
	
	@Column(name="VALUE")
	private String value;

	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public void setQuestion(MultipleChoise question) {
		this.question = question;
	}
	
}
