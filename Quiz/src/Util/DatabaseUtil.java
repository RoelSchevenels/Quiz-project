/**
 * een hulpklassen die vanalles naar de 
 * database wegschrijft en terug ophaalt
 * @author vrolijkx
 */
package Util;

import java.io.InvalidClassException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.EntityExistsException;

import main.test.Main;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import BussinesLayer.Answer;
import BussinesLayer.Jury;
import BussinesLayer.Player;
import BussinesLayer.QuestionRound;
import BussinesLayer.QuizMaster;
import BussinesLayer.Team;
import BussinesLayer.User;
import BussinesLayer.questions.MultipleChoise;
import Protocol.requests.AnswerRequest;
import Protocol.requests.CreateTeamRequest;
import Protocol.requests.CreateUserRequest;
import Protocol.requests.GetTeamsRequest;
import Protocol.requests.LoginRequest;
import Protocol.requests.TeamLoginRequest;
import Protocol.responses.AnswerResponse;
import Protocol.responses.GetTeamsResponse;
import Protocol.responses.LoginResponse;
import Protocol.responses.LoginResponse.UserType;
import Protocol.responses.TeamLoginResponse;
import Protocol.responses.TimeOutResponse;
import Protocol.submits.CorrectSubmit;

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
		s.saveOrUpdate(b);
		t.commit();
	}
	
	/**
	 * slaat alle meegegeven objecten op.
	 * een rollback wordt uitgevoerd bij een misluking.
	 * @param b
	 */
	public static void saveObjects(Object... b) {
		Session s = ConnectionUtil.getSession();
		Transaction t = s.beginTransaction();
		try {
			for(Object o:b) {
				s.saveOrUpdate(o);
			}
			t.commit();
		} catch(Exception ex) {
			//rollback uitvoeren en de exeption door geven
			t.rollback();
			throw ex;
		}
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
	
	public static Team getTeam(String TeamName, int creatorId) {
		Session s = ConnectionUtil.getSession();
		return  (Team) s.createQuery("SELECT t FROM Team t WERE t.teamCreator.id = :id AND t.teamName = :name")
					.setParameter("id", creatorId)
					.setParameter("name", TeamName)
					.uniqueResult();
	}
	
	public static Team getTeam(int teamId) {
		Session s = ConnectionUtil.getSession();
		return  (Team) s.createQuery("select t from Team t where t.teamId = :id")
					.setParameter("id", teamId)
					.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public static List<Team> getTeams(int playerId) {
		Session s = ConnectionUtil.getSession();
		return s.createQuery("select p.teams from Player p where p.id = :id")
				.setParameter("id", playerId)
				.list();
	}
	
	public static Answer getAnswer(int anwserId) {
		Session s = ConnectionUtil.getSession();
		return  (Answer) s.createQuery("select a from Answer a where a.answerId = :id")
					.setParameter("id", anwserId)
					.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public static List<Answer> getUncorrectedAnswers(int quizId, int max_amount) {
		Session s = ConnectionUtil.getSession();
		return  s.createQuery("select a from Answer a where a.jury = null and a.quiz.quizID = :id")
					.setParameter("id", quizId)
					.setMaxResults(max_amount)
					.list();
	}
	
	public static List<Answer> getUncorrectedAnswers(int quizId) {
		return getUncorrectedAnswers(quizId,10);
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
		try {
			List<Team> teams = getTeams(request.getUserId());
			
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
					
					answer.setQuestionID(a.getQuestion().getQuestionId());
					answer.setAnswerId(a.getAnswerId());
					answer.setCorrectAnswer(a.getQuestion().getCorrectAnswer());
					answer.setQuestion(a.getQuestion().getQuestion());
					answer.setGivenAnswer(a.getAnswer());
					answer.setAnswerPerson(a.getTeam().getTeamName());
					answer.send();
					
					return;
				}
			}
			
			TimeOutResponse r = new TimeOutResponse(request.getRequestId());
			r.send();
			
		} catch(HibernateException ex) {
			request.sendException("Fout bij het doorzoeken van de database");
		} catch (Exception e) {
			request.sendException("Onverwachte fout voorgedaan");
		}
	};
	
	public static void handleTeamLoginRequest(TeamLoginRequest request) {
		try {
			Team t = getTeam(request.getTeamId());
			if(!t.checkPassword(request.getPassword())) {
				request.sendException("Het wachwoord is incorrect");
				return;
			} 
			
			TeamLoginResponse r = request.createResponse();
			r.setCreatorId(t.getTeamCreator().getId());
			for(Player p: t.getPlayers()) {
				r.addPlayer(p.getId(), p.getUserName());
			}
			
			r.send();
			
		} catch(HibernateException ex) {
			request.sendException("Fout bij het doorzoeken van de database");
		} catch (Exception e) {
			request.sendException("Onverwachte fout voorgedaan");
		}
	}
	
	public static void handelCreateTeamRequest(CreateTeamRequest request) {
		try {
			Player creator = (Player) getUser(request.getCreatorId());
			
			Team t = new Team(request.getTeamName(), request.getPassword(), creator);
			
			TeamLoginResponse r = request.createResponse();
			r.setCreatorId(creator.getId());
			
			for(int id : request.getPlayerIds()) {
				Player p = (Player) getUser(id);
				t.addPlayer(p);
				r.addPlayer(p.getId(), p.getUserName());
			}
			
			saveObject(t);
			
			r.setTeamId(t.getTeamId());
			r.send();
			
			
		} catch(EntityExistsException ex) {
			request.sendException("Je hebt al een team met deze naam aangemaakt");
		} catch(HibernateException ex) {
			request.sendException("Fout bij het doorzoeken van de database");
		} catch (ClassCastException ex) {
			request.sendException("Er is iemand bij die geen speler is");
		} catch (Exception ex) {
			request.sendException("Onverwachte fout voorgedaan");
		}
	}
	
	public static void handleCorrectSubmit(CorrectSubmit submit) {
		try {
			Answer a = getAnswer(submit.getAnswerId());
			Jury j = (Jury) getUser(submit.getJurryId());
			a.correct(j,submit.getScore());
			saveObject(a);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		sendAnswers.remove(submit.getAnswerId());
	};
	
	public static void submitRound(QuestionRound round) {
		
	}
	
	public static void submitQuestion() {
		
	}
	
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
