package BuisinesLayer;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.ManyToAny;



@Entity
@DiscriminatorValue(value="MASTER")
public class QuizMaster extends User {

	@OneToMany(mappedBy="creator")
	private List<Quiz> quissen = new ArrayList<Quiz>();
	
	public QuizMaster(String userName, String password) {
		super(userName, password);
		// TODO Auto-generated constructor stub
	}

	private QuizMaster() {};
}
