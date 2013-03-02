/**
 * Test van de database configuraties
 * @author vrolijkx
 */
package main.test;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.scene.media.MediaException;

import org.hibernate.*;


import Util.ConnectionUtil;

import BussinesLayer.*;
import BussinesLayer.questions.MultipleChoise;
import BussinesLayer.questions.PictureQuestion;
import BussinesLayer.questions.Question;
import BussinesLayer.questions.StandardQuestion;
import BussinesLayer.questions.VideoQuestion;
import BussinesLayer.resources.MediaResource;
import BussinesLayer.resources.PictureResource;

/**
 * @author vrolijkx
 *
 */
public class TestDataBase {
	final static String TEST_MUSIC_PATH = "~/Desktop/02-MoneyForNothing.mp3";
	final static String TEST_MOVIE_PATH = "~/Desktop/HaroldAndKumar.mp4";
	final static String TEST_PIC_PATH   = "~/Desktop/UMLconnection.gif";
	final static File TEST_MOVIE;
	final static File TEST_MUSIC;
	final static File TEST_PIC;
	
	static {
		TEST_PIC   = new File(TEST_PIC_PATH.replace("~", System.getProperty("user.home")));
		TEST_MOVIE = new File(TEST_MOVIE_PATH.replace("~", System.getProperty("user.home")));
		TEST_MUSIC = new File(TEST_MUSIC_PATH.replace("~", System.getProperty("user.home")));
	}
	
	public static void main(String[] args) {
		try {
			ConnectionUtil.createCleanDatabase();
			ConnectionUtil.getHibernateConfiguration();
		} catch( HibernateException e) {
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
		System.out.println("Test de foto's");
		testPictures();
		System.out.println("Test de video's");
		testSaveVideoAndAudio();
		System.out.println("Test load video's"); 
		testLoadVideoAndAudio();
		
		
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
		Team team = new Team("Tafel 7","abcd123",p);
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
		
		if(te.checkPassword("abcd123")) {
			System.out.println("team " + te.getTeamName() + "password match");
		} else {
			System.out.println("fail");
		}
		
		
		t.commit();
	}

	private static void testPictures() {
		try {
			Session s = ConnectionUtil.getSession();
			Transaction t = s.beginTransaction();
			QuizMaster m = (QuizMaster) s.createCriteria(QuizMaster.class).uniqueResult();
			
			PictureQuestion p = new PictureQuestion(m);
			p.setQuestion("Hoe word dit schema genoemt");
			p.setCorrectAnswer("Object diagram");
			PictureResource pr = new PictureResource(TEST_PIC);
			p.setPicture(pr);
			
			t.commit();
			t = s.beginTransaction();
			s.save(p);
			t.commit();
			
			t = s.beginTransaction();
			PictureQuestion pic = (PictureQuestion) s.createQuery("select p from PictureQuestion p").uniqueResult();
			System.out.println(pic.getPicture().getFile());
			s.close();
			
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void testSaveVideoAndAudio() {
		QuizMaster q = new QuizMaster("tonny", "hoby");
		VideoQuestion v = new VideoQuestion(q);
		
		try {
			Session s = ConnectionUtil.getSession();
			Transaction t = s.beginTransaction();
			
			MediaResource m = new MediaResource(TEST_MOVIE);
			v.setQuestion("Welke serie is dit?");
			v.setCorrectAnswer("Engrenages");
			v.setMediaResource(m);
			
			s.save(q);
			s.save(v);
			t.commit();
			
		} catch (MediaException | IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void testLoadVideoAndAudio() {
		
		try {
			Session s = ConnectionUtil.getSession();
			Transaction t = s.beginTransaction();
			
			VideoQuestion v = (VideoQuestion) s.createQuery("select q from VideoQuestion q").uniqueResult();
			MediaResource m = v.getMediaResource();
			System.out.println(m.getSize());
			System.out.println(m.getFile().getPath());
			
			t.commit();
			
		} catch (MediaException | IOException | SQLException e) {
			e.printStackTrace();
		}
	}
}
