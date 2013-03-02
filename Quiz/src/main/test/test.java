package main.test;


import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;

import network.AutoDiscoverServer;
import network.Server;

import org.hibernate.Session;
import org.hibernate.Transaction;

import BussinesLayer.QuestionRound;
import BussinesLayer.Quiz;
import BussinesLayer.QuizMaster;
import BussinesLayer.questions.MediaQuestion;
import BussinesLayer.questions.MultipleChoise;
import BussinesLayer.questions.PictureQuestion;
import BussinesLayer.questions.Question;
import BussinesLayer.questions.StandardQuestion;
import Protocol.RequestListener;
import Protocol.RequestManager;
import Protocol.SubmitListener;
import Protocol.SubmitManager;
import Protocol.requests.GetQuizRequest;
import Protocol.requests.GetTeamsRequest;
import Protocol.requests.Request;
import Protocol.responses.GetQuizResponse;
import Protocol.submits.Submit;
import Util.ConnectionUtil;
import Util.DatabaseUtil;

public class test {

	private static long start;
	private static long end;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//CreateQuiz();
		try {
			Server.getInstance();
			AutoDiscoverServer.getInstance().start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(500, 500);
		
		
		
		QuizMaster q = (QuizMaster) DatabaseUtil.getUser("Tony");
		final Quiz quiz = q.getQuissen().get(0);
		
		RequestManager.addRequestListener(GetQuizRequest.class, new RequestListener() {
			
			@Override
			public void handleRequest(Request r) {
				System.out.println("behandelen");
				GetQuizResponse re= (GetQuizResponse) r.createResponse();
				re.setQuizId(quiz.getQuizID());
				re.setQuizName(quiz.getQuizName());
				re.send();
				
			}
		});
		
		JButton b = new JButton("stuur ronde");
		b.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				DatabaseUtil.submitRound(quiz, quiz.getRounds().get(0));
				System.out.println("Aantal Vragen: "+  quiz.getRounds().get(0).getQuestions().size());
				
			}
		});
		f.add(b,BorderLayout.CENTER);
		f.setVisible(true);

		
		
		
		
	}
	
	private static Quiz CreateQuiz() {
		try {
			ConnectionUtil.StartDataBase();
			
			Session s = ConnectionUtil.getSession();
			Transaction t = s.beginTransaction();
			
			QuizMaster tony = new QuizMaster("Tony", "Tony");
	        QuestionRound ronde1 = new QuestionRound("De eerste ronde");
	        QuestionRound ronde2 = new QuestionRound("De totaal arbitraire winaars aanduidings ronde");
	        QuestionRound ronde3 = new QuestionRound("De finale");
	        
	        s.save(tony);
	        s.save(ronde1);
	        s.save(ronde2);
	        s.save(ronde3);
	        
	        Question q1 = new StandardQuestion(tony);
	        q1.setQuestion("Wat zijn de beste koeken van Lu?");
	        q1.setCorrectAnswer("Dinosoarus koeken");

	        s.save(q1);
	        
	        
	        PictureQuestion pic = (PictureQuestion) s.createQuery("select p from PictureQuestion p").uniqueResult();
	        MediaQuestion med = (MediaQuestion) s.createQuery("select m from VideoQuestion m").uniqueResult();
	        System.out.println(med.getQuestion());
	        
	        MultipleChoise q2 = new MultipleChoise(tony);
	        q2.setQuestion("Wat is het doel van de tilburgse duivenSport?");
	        q2.addValue("het neerschieten van duiven");
	        q2.addValue("het pikken van andermans duiven");
	        q2.addValue("Een zo goed mogelijke imitatie van een duif neerzetten");
	        q2.setCorrectAnswer("het pikken van andermans duiven");
	       
	        s.save(q2);
	        
	        q1.addQuestionRound(ronde2);
	        q2.addQuestionRound(ronde3);
	        ronde1.addQuestion(q1);
	        ronde1.addQuestion(pic);
	        ronde1.addQuestion(med);
	        pic.addQuestionRound(ronde3);

	        Quiz q = new Quiz("De quiz", tony);
	        q.addRound(ronde1);
	        q.addRound(ronde2);
	        q.addRound(ronde3);
	        
	        s.save(q);
	        
	        t.commit();
	        s.close();
	        
	        
	        return q;
			
		} catch(Exception e) {
			e.printStackTrace();
		} 
		
		return null;
		
		
	}

}
