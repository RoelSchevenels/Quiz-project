package GUI.quizMaster;


import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.hibernate.Session;

import com.sun.javafx.tk.Toolkit;

import screenManger.ScreenManeger;

import Util.ConnectionUtil;

import BussinesLayer.QuestionRound;
import BussinesLayer.Quiz;
import BussinesLayer.QuizMaster;
import BussinesLayer.questions.MediaQuestion;
import BussinesLayer.questions.MultipleChoise;
import BussinesLayer.questions.PictureQuestion;
import BussinesLayer.questions.Question;
import BussinesLayer.questions.StandardQuestion;

import javaFXToSwing.PaneToPanel;
import javaFXpanels.MediaPane.MediaPlayerPane;
import javafx.application.Application;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestQuizMasterPanel extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		
		
		Scene scene = new Scene(new QuizMasterDisplay(createTestQuiz()));
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	
	private static Quiz createTestQuiz() {
		QuizMaster tony = new QuizMaster("Tony", "Tony");
		QuestionRound ronde1 = new QuestionRound("De eerste ronde");
		QuestionRound ronde2 = new QuestionRound("De totaal arbitraire winaars aanduidings ronde");
		QuestionRound ronde3 = new QuestionRound("De finale");
		
		Question q1 = new StandardQuestion(tony);
		q1.setQuestion("Wat zijn de beste koeken van Lu?");
		q1.setCorrectAnswer("Dinosoarus koeken");
		
		Session s = ConnectionUtil.getSession();
		PictureQuestion pic = (PictureQuestion) s.createQuery("select p from PictureQuestion p").uniqueResult();
		MediaQuestion med = (MediaQuestion) s.createQuery("select m from VideoQuestion m").uniqueResult();
		System.out.println(med.getQuestion());
		MultipleChoise q2 = new MultipleChoise(tony);
		q2.setQuestion("Wat is het doel van de tilburgse duivenSport?");
		q2.addValue("het neerschieten van duiven");
		q2.addValue("het pikken van andermans duiven");
		q2.addValue("Een zo goed mogelijke imitatie van een duif neerzetten");
		q2.setCorrectAnswer("het pikken van andermans duiven");
		
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
		
		return q;
	}

	public static void main(String[] args) {
		//launch(args);
		JFrame f = ScreenManeger.getInstance().getFrame("quiz");
		f.setSize(800, 800);
		f.setVisible(true);
		
		QuizMasterDisplay q = new QuizMasterDisplay(createTestQuiz());
		
		PaneToPanel<QuizMasterDisplay> qDip = new PaneToPanel<QuizMasterDisplay>(q);
		
		f.setLayout(new BorderLayout());
		f.add(qDip,"Center");
		
		
		f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
	}
}
