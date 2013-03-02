/**
 * @author vrolijkx;
 * 
 */
package javaFXpanels.CreateQuiz;

import java.net.URL;
import java.util.ResourceBundle;
import javaFXpanels.MessageProvider.MessageProvider;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import Util.ConnectionUtil;
import Util.DatabaseUtil;

import BussinesLayer.QuestionRound;
import BussinesLayer.Quiz;
import BussinesLayer.QuizMaster;
import BussinesLayer.questions.Question;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.AnchorPane;

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
        session = ConnectionUtil.getSession();
        initBindings();
        root.getChildren().removeAll(createQuizPane,createRoundsPane);
        createQuizPane.setVisible(true);
        createRoundsPane.setVisible(true);
        
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
    		transaction = session.beginTransaction();
    		QuestionRound round = new QuestionRound(roundsText.getText());
    		session.saveOrUpdate(round);
    		currentQuiz.addRound(round);
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
    	} else {
    		messageMaker.showPane(createQuestionPane);
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
    		
    		transaction = session.beginTransaction();
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
    	quisses.getItems().setAll(q.getQuissen());
    	//session.saveOrUpdate(q);
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
