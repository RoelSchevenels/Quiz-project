package BuisinesLayer;

import javax.persistence.*;

@Entity
@DiscriminatorValue(value="JURY")
public class Jury extends User {

	public Jury(String userName, String password) {
		super(userName, password);
		// TODO Auto-generated constructor stub
	}
	
	private Jury() {
		
	}
	
}
