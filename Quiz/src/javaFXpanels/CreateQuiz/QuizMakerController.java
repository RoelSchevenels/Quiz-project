/**
 * @author vrolijkx;
 * 
 */
package javaFXpanels.CreateQuiz;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javaFXpanels.MessageProvider.MessageProvider;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.AnchorPane;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import BussinesLayer.QuestionRound;
import BussinesLayer.Quiz;
import BussinesLayer.QuizMaster;
import BussinesLayer.questions.Question;
import Util.ConnectionUtil;

/**
 * FXML Controller class
 *
 * @author Vrolijk Kristof <Vrolijkx.Kristof@gmail.com>
 */
public class QuizMakerController implements Initializable {
	@FXML
	private AnchorPane root;
	@FXML
	private AnchorPane createQuizPane;
	@FXML
	private AnchorPane createRoundsPane;
	@FXML
	private ListView<Quiz> quisses;
	@FXML
	private ListView<QuestionRound> rounds;
	@FXML
	private ListView<Question> questions;
	@FXML
	private TextField quizText;
	@FXML
	private TextField minTeamsText;
	@FXML
	private TextField maxTeamsText;
	@FXML
	private TextArea locationText;
	@FXML
	private TextField roundsText;
	private AnchorPane createQuestionPane;
	private CreateQuestionController createQuestionController;
	
	private MessageProvider messageMaker;
	private Session session;
	private Transaction transaction;
	private QuizMaster quizMaster;
	private Quiz currentQuiz;
	private QuestionRound currentRound;
	
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        messageMaker = new MessageProvider(root);
        initBindings();
        root.getChildren().removeAll(createQuizPane,createRoundsPane);
        createQuizPane.setVisible(true);
        createRoundsPane.setVisible(true);
        loadQuestionMaker();
    }
    
    private void loadQuestionMaker() {
    	try {
    		FXMLLoader loader = new FXMLLoader();
    		loader.setLocation(CreateQuestionController.class.getResource("createQuestion.fxml"));
    		createQuestionPane = (AnchorPane) loader.load();
    		createQuestionController = loader.getController();
    		createQuestionController.setOnCancel(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent event) {
					cancel();
				}
			});
    		createQuestionController.setOnFinish(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					AddQuestion(createQuestionController.getCurrentQuestion());
					
				}
			});
    		
    	} catch( IOException e) {
    		messageMaker.showError("Kan vragen panneel niet laden");
    	}	
    }
      
    private void initBindings() {	
    	//zorgen dat er maar 1 geselecteerd kan worden
    	quisses.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    	rounds.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    	questions.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    	
    	quisses.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Quiz>() {
			@Override
			public void changed(ObservableValue<? extends Quiz> observable,
					Quiz oldValue, Quiz newValue) {
				if(newValue != null) {
					rounds.getItems().setAll(newValue.getRounds());
					currentQuiz = newValue;
				} else {
					rounds.getItems().clear();
				}
			}
		});
    	
    	rounds.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QuestionRound>() {	
			@Override
			public void changed(
					ObservableValue<? extends QuestionRound> observable,
					QuestionRound oldValue, QuestionRound newValue) {
				if(newValue!=null) {
					questions.getItems().setAll(newValue.getQuestions());
					currentRound = newValue;
				} else {
					questions.getItems().clear();
				}
				
			}
		});	
    }
    
    private void AddQuestion(Question q) {
    	session = ConnectionUtil.getSession();
    	transaction = session.beginTransaction();
    	session.saveOrUpdate(q);
    	currentRound.addQuestion(q);
    	session.saveOrUpdate(currentRound);
    	
    	try {
    		transaction.commit();
    		questions.getItems().add(q);
    		messageMaker.hide();
    	} catch(HibernateException ex) {
    		ex.printStackTrace();
    		messageMaker.showError("kon niet aan de database toevoegen");
    	}
    }
    
    @FXML
    private void addRoundPressed() {
    	if(currentQuiz == null) {
    		messageMaker.showWarning("Eerst een quiz selecteren");
    	} else {
    		messageMaker.showPane(createRoundsPane);
    	}
    }
    
    @FXML
    private void createRoundPressed() {
    	if(validateText(roundsText)) {
    		session = ConnectionUtil.getSession();
    		transaction = session.beginTransaction();
    		QuestionRound round = new QuestionRound(roundsText.getText());
    		round.addQuiz(currentQuiz);
    		session.saveOrUpdate(round);
    		session.saveOrUpdate(currentQuiz);
    		try {
    			transaction.commit();
    		} catch(HibernateException ex) {
    			messageMaker.showWarning("kon de ronde niet in de database\nopslaan.");
    			return;
    		}
    		
    		rounds.getItems().add(round);
    		messageMaker.hide();
    	}
    }
    
    @FXML
    private void addQuestionPressed() {
    	if(currentRound == null) {
    		messageMaker.showWarning("Eerst een ronde selecteren");
    	} else if(createQuestionPane != null) {
    		createQuestionController.show(quizMaster);
    		messageMaker.showPane(createQuestionPane);	
    	} else {
    		messageMaker.showError("kan geen vragen aanmaken");
    	}
    	
    }
    
    @FXML
    private void addQuizPressed() {
    	messageMaker.showPane(createQuizPane);
    }
    
    @FXML
    private void creatQuizPressed() {
    	if(quizMaster == null) {
    		messageMaker.showError("setQuisMaster aanroepen graag");
    		return;
    	}
    	int maxTeams;
    	int minTeams;
    	if(validateText(quizText,minTeamsText,maxTeamsText,locationText)) {
    		try {
    			maxTeams = Integer.parseInt(minTeamsText.getText());
    			minTeams = Integer.parseInt(maxTeamsText.getText());
    		} catch(NumberFormatException ex) {
    			messageMaker.showWarning("min en max aantal teams\nmoeten nummers zijn!");
    			return;
    		}
    		
    		transaction = ConnectionUtil.getSession().beginTransaction();
    		Quiz q = new Quiz(quizText.getText(), quizMaster);
    		q.setMaxTeams(maxTeams);
    		q.setMinTeams(minTeams);
    		session.save(q);
    		try {
    			transaction.commit();
    		} catch(HibernateException ex) {
    			messageMaker.showError("kon quiz niet aanmaken in de database");
    			return;
    		}
    		
    		messageMaker.hide();
    		quisses.getItems().add(q);
    		
    	} else {
    		messageMaker.showWarning("Gelieve alle velden\nin te voeren");
    	}
    }
    
    @FXML
    private void cancel() {
    	messageMaker.hide();
    }
    
    public void setQuizMaster(QuizMaster q) {
    	session = ConnectionUtil.getSession();
    	transaction = session.beginTransaction();
    	session.saveOrUpdate(q);
    	try {
    		transaction.commit();
    	} catch (Exception ex) {
    		transaction.rollback();
    		session.saveOrUpdate(q);
    	}
    	quisses.getItems().setAll(q.getQuissen());
    	quizMaster = q;
    	
    }
    
    private boolean validateText(TextInputControl... fields) {
    	for(TextInputControl t: fields) {
    		if(t.getText().isEmpty()) {
    			messageMaker.showWarning("niet alle velden ingevuld");
    			return false;
    		}
    	}
    	return true;
    }
    
   
}
