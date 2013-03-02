/**
 * @author vrolijkx;
 * 
 */
package javaFXpanels.CreateQuiz;

import java.net.URL;
import java.util.ResourceBundle;
import javaFXpanels.MessageProvider.MessageProvider;

import org.hibernate.Session;
import org.hibernate.Transaction;

import Util.ConnectionUtil;

import BussinesLayer.QuestionRound;
import BussinesLayer.Quiz;
import BussinesLayer.questions.Question;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
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
	private ListView<Quiz> quisses;
	@FXML
	private ListView<QuestionRound> rounds;
	@FXML
	private ListView<Question> questions;
	private MessageProvider messageMaker;
	
	private Session session;
	private Transaction transaction;
	
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        messageMaker = new MessageProvider(root);
        session = ConnectionUtil.getSession();
        transaction = session.beginTransaction();
        initBindings();
        
    }
    
    private void initBindings() {
    	
    	//zorgen dat er maar 1 geselecteerd kan worden
    	quisses.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    	rounds.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    	questions.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    	
    	quisses.getSelectionModel().selectedItemProperty();
    	
    }
    
    @FXML
    private void addRoundPressed() {
    	
    }
    
    @FXML
    private void addQuestionPressed() {
    	
    }
    
    @FXML
    private void addQuizPressed() {
    	
    }
    
   
}
