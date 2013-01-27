/**
 * een mogelijkheid die vasthangt aan een multiplechoisevraag
 * @author vrolijkx
 */
package BuisinesLayer.questions;

import javax.persistence.*;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;


@Entity
@Table(name="MULTIPLECHOISE")
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
