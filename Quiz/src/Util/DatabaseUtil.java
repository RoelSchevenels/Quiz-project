package Util;

import java.util.List;

import javax.persistence.EntityExistsException;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.internal.StaticFilterAliasGenerator;

import BussinesLayer.Jury;
import BussinesLayer.Player;
import BussinesLayer.QuizMaster;
import BussinesLayer.Team;
import BussinesLayer.User;
import Protocol.CreateUserRequest;
import Protocol.GetTeamsRequest;
import Protocol.GetTeamsResponse;
import Protocol.LoginRequest;
import Protocol.LoginResponse;
import Protocol.LoginResponse.UserType;

public class DatabaseUtil {

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
	
	public static User getUser(String username) {
		Session s = ConnectionUtil.getSession();
		return  (User) s.createQuery("select u from User u where u.userName = :name")
					.setParameter("name", username)
					.uniqueResult();
	}
	
	public static User getUser(int userId) {
		Session s = ConnectionUtil.getSession();
		return  (User) s.createQuery("select u from User u where u.id = :id")
					.setParameter("id", userId)
					.uniqueResult();
	}
	
	public static void handleLogin(LoginRequest request) {

		try {
			User user = getUser(request.getUserName());
			
			if(user == null) {
				request.sendException("De user bestaat niet");
				return;
			} else if(!user.checkPassword(request.getPassword())) {
				request.sendException("Incorrect passwoord");
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
		} catch (Exception e) {
			request.sendException("Onverwachte fout voorgedaan");
		}	
	
	}

	public static void handleGetTeams(GetTeamsRequest request) {
		Session s = ConnectionUtil.getSession();

		try {
			@SuppressWarnings("unchecked")
			List<Team> teams = s.createQuery("select p.teams from Player p where p.id = :requestId")
					.setParameter("requestId", request.getUserId())
					.list();
			
			GetTeamsResponse r = (GetTeamsResponse) request.createResponse();
			
			for(Team t: teams) {
				r.addTeamItem(t.getTeamName(), t.getTeamId());
			}
			
			r.send();
			
		} catch(HibernateException ex) {
			request.sendException("Fout bij het doorzoeken van de database");
		} catch (Exception e) {
			request.sendException("Onverwachte fout voorgedaan");
		}

	}

	public static void handleCreateUser(CreateUserRequest request) {
		User user;
		try {
			if(request.getUserType().equals(UserType.PLAYER)) {
				user = new Player(request.getUserName(), request.getPassword());
			} else if(request.getUserType().equals(UserType.JURRY)) {
				user = new Jury(request.getUserName(), request.getPassword());	
			} else {
				//hier gebeurt iets wat we niet verwachten
				throw new Exception();
			}
			
			user.setFirstName(request.getFirstName());
			user.setLastName(request.getLastName());
			user.setEmail(request.getEmail());
			
			saveObject(user);
			

			LoginResponse r = (LoginResponse) request.createResponse();
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
			
		} catch(EntityExistsException ex) {
			request.sendException("Deze username is al gebruikt");
		} catch(HibernateException ex) {
			request.sendException("Kon de gebruiker niet aanmaken");
		} catch (Exception e) {
			request.sendException("Onverwachte fout voorgedaan");
		}
	}
	
	
}
