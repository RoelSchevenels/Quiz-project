/**
 * een quizmaster die de vragen gaat lezen en opstellen
 * @author vrolijkx
 */
package BussinesLayer;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
@DiscriminatorValue(value="MASTER")
public class QuizMaster extends User {

	@OneToMany(mappedBy="creator")
	private List<Quiz> quissen = new ArrayList<Quiz>();
	
	public QuizMaster(String userName, String password) {
		super(userName, password);
	}

	@SuppressWarnings("unused")
	private QuizMaster() {}; //hibernate Constructor

	public List<Quiz> getQuissen() {
		return quissen;
	}
}
