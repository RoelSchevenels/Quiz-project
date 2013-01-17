/**
 * Test van de database configuraties
 * @author vrolijkx
 */
package main.test;

import java.util.ArrayList;

import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.tool.hbm2ddl.SchemaExport;

import BuisinesLayer.*;
import BuisinesLayer.questions.MultipleChoise;
import BuisinesLayer.questions.Question;
import BuisinesLayer.questions.StandardQuestion;

/**
 * @author vrolijkx
 *
 */
public class TestDataBase {
	private static SessionFactory sessionFactory;
	private static Configuration configuration;
	
	public static SessionFactory configureSessionFactory() throws HibernateException {
		if (sessionFactory == null) {
			configuration = new Configuration().configure();
			
			ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();        
			sessionFactory = configuration.buildSessionFactory(serviceRegistry);
			
			
			
		}
	    return sessionFactory;
	}
	
	public static void createCleanDatabase() {
		if(configuration!=null) {
			SchemaExport export = new SchemaExport(configuration);
			export.setOutputFile("/Users/vrolijkx/Desktop/test.sql");
			export.create(true,true);
		}
	}
	
	public static Session getSession() {
		if (sessionFactory == null) {
			configureSessionFactory();
		}
		
		try {
			return sessionFactory.getCurrentSession();
		} catch(HibernateException e) {
			return sessionFactory.getCurrentSession();
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		configureSessionFactory();
		createCleanDatabase();
		
		Session s = sessionFactory.openSession();
		Transaction t = s.beginTransaction();
		
		Jury j = new Jury("TonnyKlusser5","wachtwoord");
		j.setFirstName("Dwaas");
		j.setLastName("Stalmans");
		
		Player p = new Player("Tonny5","wachtwoord");
		p.setFirstName("Jan");
		p.setLastName("Bervoets");
		
		QuizMaster m = new QuizMaster("CanvasCrack5","hermando");
		m.setFirstName("Herman");
		m.setLastName("Vanmolle");
		
		Team team = new Team("Tafel 7","abcd123");
		team.addPlayer(p);
		
		Quiz quiz = new Quiz("test quiz", m);
		quiz.addTeam(team);
		
		s.saveOrUpdate(j);
		s.saveOrUpdate(m);
		s.save(team);
		s.save(p);
		s.saveOrUpdate(quiz);
		
		t.commit();
		
		//TEST
		//opvragen van de opgeslagen users
		t=s.beginTransaction();
		
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
		
		
		t.commit();
		System.out.println("deel 1 gelukt");
		s.close();
		
		
		//nieuwe sessie openen en team tafel 7 proberen te zoeken.
		s=sessionFactory.openSession();
		t=s.beginTransaction();
		
		Team te = (Team) s.createQuery("select t from Team t where t.teamName = 'Tafel 7'").uniqueResult();
		
		if(te.hassPassword("abcd123")) {
			System.out.println("team " + te.getTeamName() + "password match");
		} else {
			System.out.println("fail");
		}
		
		
		
		t.commit();
		
		//nieuwe transactie waar een paar vragen worden aangemaakt en in een ronde gestoken
		
		t=s.beginTransaction();
		
		QuestionRound round = new QuestionRound("De eerste Ronde");
		
		MultipleChoise q1 = new MultipleChoise(m);
		q1.addValue("a) Steve Vanherwegge");
		q1.addValue("b) Bart de Pauw");
		q1.addValue("c) Herman Vanmolle");
		q1.setQuestion("Wie is de beste Quizmaster van de vrt?");
		q1.setCorrectAnswer("c) Herman Vanmolle");
		
		StandardQuestion q2 = new StandardQuestion(m);
		q2.setQuestion("Op welke dag valt kerstmis");
		q2.setCorrectAnswer("25 december");
		
		
		round.addQuestion(q1);
		round.addQuestion(q2);
		
		s.save(q1);
		s.save(q2);
		s.save(round);
		t.commit();
		
		//einde van de testen
		s.close();
		sessionFactory.close();
	}

}
