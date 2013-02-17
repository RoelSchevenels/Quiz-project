/**
 * Business object dat alle soorten van users bevat
 * superklass van onderander 
 * <ul><li>Player</li> 
 * <li>Jury<li>
 * <li>QuizMaster</li></ul>
 * 
 * @author vrolijkx
 */

package BuisinesLayer;

import javax.persistence.*;

import Util.Security;

@Entity
@DiscriminatorColumn(name="userType")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public abstract class User {
	@Id
	@Column(name="USER_ID",length=20)
	protected String userName;
	@Column(name="FIRST_NAME",length=20)
	protected String firstName;
	@Column(name="LAST_NAME",length=20)
	protected String LastName;
	@Column(name="EMAIL",length=256,unique=true)
	protected String Email;
	
	@Column(name="SALT",length=80)
	private String salt;
	@Column(name="PASS",length=50)
	private String password;
	
	/**
	 * De constructor zonder argumenten voor hibernate
	 */
	public User() {	
	};
	
	public User(String userName,String password) {
		this.userName = userName;
		this.salt = Security.getRandomSalt();
		this.password = Security.encrypt(password, salt);
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return LastName;
	}
	
	public void setLastName(String lastName) {
		LastName = lastName;
	}
	
	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}
	
	
}
