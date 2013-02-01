/**
 * een jurry implementatie 
 * @author vrolijkx
 */
package BuisinesLayer;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
@DiscriminatorValue(value="JURY")
public class Jury extends User {
	@OneToMany(mappedBy="jury")
	List<Answer> correctedAnswers = new ArrayList<Answer>();
	
	public Jury(String userName, String password) {
		super(userName, password);
		// TODO Auto-generated constructor stub
	}
	
	/*
	 *Constructor voor hibernate 
	 */
	@SuppressWarnings("unused")
	private Jury() {}

	public void addAnswer(Answer answer) {
		if(!correctedAnswers.contains(answer)) {
			this.correctedAnswers.add(answer);
		}
	}
	
}