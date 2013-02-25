/**
 * een hulpklassen die vanalles naar de 
 * database wegschrijft en terug ophaalt
 * @author vrolijkx
 */
package Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.EntityExistsException;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sun.javafx.css.converters.EnumConverter;

import BussinesLayer.Answer;
import BussinesLayer.Jury;
import BussinesLayer.Player;
import BussinesLayer.QuizMaster;
import BussinesLayer.Team;
import BussinesLayer.User;
import BussinesLayer.questions.MultipleChoise;
import Protocol.requests.AnswerRequest;
import Protocol.requests.CreateUserRequest;
import Protocol.requests.GetTeamsRequest;
import Protocol.requests.LoginRequest;
import Protocol.responses.AnswerResponse;
import Protocol.responses.GetTeamsResponse;
import Protocol.responses.LoginResponse;
import Protocol.responses.LoginResponse.UserType;

public class DatabaseUtil {
	//map met Al verzonden antwoorden
	private static ConcurrentHashMap<Integer, Answer> sendAnswers = new ConcurrentHashMap<Integer, Answer>();
	
	
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
	
	public static Team getTeam(int teamId) {
		Session s = ConnectionUtil.getSession();
		return  (Team) s.createQuery("select t from Team t where t.teamId = :id")
					.setParameter("id", teamId)
					.uniqueResult();
	}
	
	public static Answer getAnwer(int anwserId) {
		Session s = ConnectionUtil.getSession();
		return  (Answer) s.createQuery("select a from Answer a where a.answerId = :id")
					.setParameter("id", anwserId)
					.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public static List<Answer> getUncorrectedAnswers(int quizId) {
		Session s = ConnectionUtil.getSession();
		return  s.createQuery("select a from Answer a where a.jury = null and a.quiz.quizID = :id")
					.setParameter("id", quizId)
					.setMaxResults(20)
					.list();
	}
	
	public static void handleLoginRequest(LoginRequest request) {
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

	public static void handleGetTeamsRequest(GetTeamsRequest request) {
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

	public static void handleCreateUserRequest(CreateUserRequest request) {
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
	
	public static void handleAnswerRequest(AnswerRequest request) {
		try {
			for(Answer a: getUncorrectedAnswers(request.getQuizId())) {
				if(!sendAnswers.containsKey(a.getAnswerId())) {
					sendAnswers.put(a.getAnswerId(), a);
					AnswerResponse answer = request.createResponse();
					//Todo: answerResponse invullen
					
					answer.send();
					return;
				}
			}
			
		} catch(HibernateException ex) {
			request.sendException("Fout bij het doorzoeken van de database");
		} catch (Exception e) {
			request.sendException("Onverwachte fout voorgedaan");
		}
	};
	
	public static void correctMultipleChoise(Answer a) {
		if(!(a.getQuestion() instanceof MultipleChoise)) {
			throw new IllegalArgumentException("kan alleen multiple choise exceptions verbeteren");
		}
		
		Jury computer = (Jury) getUser("Computer");
		MultipleChoise q = (MultipleChoise) a.getQuestion();
		
		if(a.getAnswer().equals(q.getCorrectAnswer())) {
			a.correct(computer, a.getMaxScore());
		} else {
			a.correct(computer, 0);
		}
		
		saveObject(a);
	}

}
