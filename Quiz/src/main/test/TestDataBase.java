/**
 * Test van de database configuraties
 * @author vrolijkx
 */
package main.test;

import java.util.ArrayList;

import org.hibernate.*;

import Util.ConnectionUtil;

import BuisinesLayer.*;
import BuisinesLayer.questions.MultipleChoise;
import BuisinesLayer.questions.Question;
import BuisinesLayer.questions.StandardQuestion;

/**
 * @author vrolijkx
 *
 */
public class TestDataBase {
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ConnectionUtil.configureSessionFactory();
			ConnectionUtil.createCleanDatabase();
		} catch( HibernateException e) {
			System.out.println("het lukt niet");
			e.printStackTrace();
			return;
		}
		
		System.out.println("Start users toevoegen");
		testUsers();
		System.out.println("Start quiz aanmaken");
		testTeamQuiz();
		System.out.println("start quiz querien");
		querieExample();
		System.out.println("Vragen toevoegen");
		createQuestions();
		System.out.println("Vragen querieen");
		querieQuestions();
		ConnectionUtil.CloseSessionFactory();
	}

	private static void querieQuestions() {
		Session s = ConnectionUtil.getSession();
		
		@SuppressWarnings("unchecked")
		ArrayList<Question> questions = (ArrayList<Question>) s.createQuery("select q from Question q").list();
		
		for(Question q: questions) {
			System.out.println(q.getQuestion() + "\t" + q.getCorrectAnswer());
		}
		
	}

	private static void createQuestions() {
		Session s = ConnectionUtil.getSession();
		Transaction t = s.beginTransaction();
		QuizMaster m = (QuizMaster) s.createCriteria(QuizMaster.class).uniqueResult();
		
		MultipleChoise q1 = new MultipleChoise(m);
		q1.addValue("a) Steve Vanherwegge");
		q1.addValue("b) Bart de Pauw");
		q1.addValue("c) Herman Vanmolle");
		q1.setQuestion("Wie is de beste Quizmaster van de vrt?");
		q1.setCorrectAnswer("c) Herman Vanmolle");
		
		StandardQuestion q2 = new StandardQuestion(m);
		q2.setQuestion("Op welke dag valt kerstmis");
		q2.setCorrectAnswer("25 december");
		
		s.save(q1);
		s.save(q2);
		t.commit();
		s.close();
	}

	private static void testTeamQuiz() {
		Session s = ConnectionUtil.getSession();
		Transaction t = s.beginTransaction();
		
		Player p = (Player) s.createQuery("SELECT p FROM Player p").uniqueResult();
		Team team = new Team("Tafel 7","abcd123");
		team.addPlayer(p);
		
		QuizMaster m = (QuizMaster) s.createCriteria(QuizMaster.class).uniqueResult();
		Quiz quiz = new Quiz("test quiz", m);
		quiz.addTeam(team);
		
		

		s.save(team);
		s.saveOrUpdate(quiz);
		t.commit();
	}

	private static void testUsers() {
		Session s = ConnectionUtil.getNewSession();
		Transaction t = s.beginTransaction();
		
		Jury j = new Jury("TonnyKlusser5","wachtwoord");
		j.setFirstName("Dwaas");
		j.setLastName("Stalmans");
		
		Player p = new Player("Tonny5","wachtwoord");
		p.setEmail("www.tof@g.be");
		p.setFirstName("Jan");
		p.setLastName("Bervoets");
		
		QuizMaster m = new QuizMaster("CanvasCrack5","hermando");
		m.setFirstName("Herman");
		m.setLastName("Vanmolle");
		
		s.saveOrUpdate(j);
		s.saveOrUpdate(m);
		s.save(p);
		t.commit();
		s.close();
	}

	private static void querieExample () {
		Session s = ConnectionUtil.getSession();
		Transaction t = s.beginTransaction();
		
		@SuppressWarnings("unchecked")
		ArrayList<User> players = (ArrayList<User>) s.createQuery("from User p order by p.firstName asc").list();
		
		for(User pe: players) {
			System.out.println(pe.getFirstName());
			if(pe instanceof Player) {
				System.out.println("is een player");
			} else if(pe instanceof Jury) {
				System.out.println("Verbeterd de vragen");
			} else {
				System.out.println("Stelt de vragen");
				pe.setFirstName("Herman");
			}
		}
		
		
		Team te = (Team) s.createQuery("select t from Team t where t.teamName = 'Tafel 7'").uniqueResult();
		
		if(te.hassPassword("abcd123")) {
			System.out.println("team " + te.getTeamName() + "password match");
		} else {
			System.out.println("fail");
		}
		
		
		t.commit();
	}
}
