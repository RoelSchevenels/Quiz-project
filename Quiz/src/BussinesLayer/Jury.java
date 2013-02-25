/**
 * een jurry implementatie 
 * @author vrolijkx
 */
package BussinesLayer;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
@DiscriminatorValue(value="JURY")
public class Jury extends User {
	@OneToMany(mappedBy="jury")
	List<Answer> correctedAnswers = new ArrayList<Answer>();
	
	public Jury(String userName, String password) {
		super(userName, password);
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
