/**
 * Business object dat alle soorten van users bevat
 * superklass van onderander 
 * <ul><li>Player</li> 
 * <li>Jury<li>
 * <li>QuizMaster</li></ul>
 * 
 * @author vrolijkx
 */

package BussinesLayer;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import Util.Security;

@Entity
@DiscriminatorColumn(name="userType")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public abstract class User {
	@Id
	@GeneratedValue
	private int id;
	@Column(name="USER_NAME",length=20,unique=true)
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
	
	public boolean checkPassword(String password) {
		return Security.checkPassword(password, this.salt, this.password);
	}
	
	public String getUserName() {
		return userName;
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
	
	public int getId() {
		return id;
	}	
}
