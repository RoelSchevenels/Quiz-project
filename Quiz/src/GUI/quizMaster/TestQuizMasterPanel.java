package GUI.quizMaster;

import java.util.Random;

import org.hibernate.Session;
import org.hibernate.Transaction;

import Util.ConnectionUtil;

import BuisinesLayer.QuestionRound;
import BuisinesLayer.Quiz;
import BuisinesLayer.QuizMaster;
import BuisinesLayer.questions.MultipleChoise;
import BuisinesLayer.questions.PictureQuestion;
import BuisinesLayer.questions.Question;
import BuisinesLayer.questions.StandardQuestion;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestQuizMasterPanel extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		QuizMaster tony = new QuizMaster("Tony", "Tony");
		QuestionRound ronde1 = new QuestionRound("De eerste ronde");
		QuestionRound ronde2 = new QuestionRound("De totaal arbitraire winaars aanduidings ronde");
		QuestionRound ronde3 = new QuestionRound("De finale");
		
		Question q1 = new StandardQuestion(tony);
		q1.setQuestion("Wat zijn de beste koeken van Lu?");
		q1.setCorrectAnswer("Dinosoarus koeken");
		
		Session s = ConnectionUtil.getSession();
		PictureQuestion pic = (PictureQuestion) s.createQuery("select p from PictureQuestion p").uniqueResult();
		
		
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
		pic.addQuestionRound(ronde3);
		
		Quiz q = new Quiz("De quiz", tony);
		q.addRound(ronde1);
		q.addRound(ronde2);
		q.addRound(ronde3);
		
		Scene scene = new Scene(new QuizMasterDisplay(q));
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}

	public static void main(String[] args) {
		launch(args);
	}
}
