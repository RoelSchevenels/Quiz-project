package Util;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import BussinesLayer.Jury;
import BussinesLayer.Player;
import BussinesLayer.QuizMaster;
import BussinesLayer.User;
import Protocol.LoginRequest;
import Protocol.LoginResponse;
import Protocol.UserSubmit;
import Protocol.LoginResponse.UserType;

public class DatabaseUtil {
	{
		ConnectionUtil.StartDataBase();
	}

	/**
	 * sla een object op in de database
	 * @param b object to be saved in the datbase
	 */
	public static void saveObject(Object b) {
		Session s = ConnectionUtil.getSession();
		Transaction t = s.beginTransaction();
		s.save(b);
		t.commit();
	}
	
	public static void handleLogin(LoginRequest request) {
		Session s = ConnectionUtil.getSession();
		
		try {
			User user = (User) s.createQuery("select u from User u where u.userName = :name")
			.setParameter("name", request.getUserName())
			.uniqueResult();
			
			if(user == null) {
				request.sendException("De user bestaat niet");
				return;
			} 
			
			LoginResponse r = request.createResponse();
			r.setEmail(user.getEmail());
			r.setFirstname(user.getFirstName());
			r.setLastName(user.getLastName());
			r.setUserId(user.getId());
			
			if(user instanceof Jury) {
				r.setUserType(UserType.JURRY);
			} else if(user instanceof Player) {
				r.setUserType(UserType.PLAYER);
			} else if(user instanceof QuizMaster) {
				request.sendException("Als quizmaster moet u op de server inloggen");
				return;
			}
			r.send();
			
			
		} catch(HibernateException ex) {
			request.sendException("Fout bij het doorzoeken van de database");
		}
	}

	public static void handleUserSubmit(UserSubmit submit) {
		
	}
}
