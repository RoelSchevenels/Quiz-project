/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javaFXpanels.Jury;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javaFXpanels.MessageProvider.MessageProvider;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import network.Client;
import Protocol.FxResponseListener;
import Protocol.exceptions.IdRangeException;
import Protocol.requests.AnswerRequest;
import Protocol.responses.AnswerResponse;
import Protocol.responses.ExceptionResponse;
import Protocol.responses.GetQuizResponse;
import Protocol.responses.LoginResponse;
import Protocol.responses.LoginResponse.UserType;
import Protocol.responses.Response;
import Protocol.responses.TimeOutResponse;
import Protocol.submits.CorrectSubmit;

/**
 * FXML Controller class
 *
 * @author Vrolijk Kristof <Vrolijkx.Kristof@gmail.com>
 */
public class CorrectionController implements Initializable {
    @FXML
    private AnchorPane root;
    @FXML
    private ScrollPane correctionPane;
    @FXML
    private VBox correctionBox;
    @FXML
    private StackPane questionBox;
    @FXML
    private StackPane correctAnswerBox;
    @FXML
    private StackPane answerBox;
    @FXML
    private Text questionText;
    @FXML
    private Text correctAnswerText;
    @FXML
    private Text answerText;
    @FXML
    private Button correctButton;
    @FXML
    private Button wrongButton;
    @FXML
    private Button refreshButton;
    private LoginResponse jury;
    private GetQuizResponse quiz;
    private AnswerResponse answer;
    private MessageProvider messageMaker;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	messageMaker = new MessageProvider(root);
    	initBindings();
    }   
    
    private void initBindings() {
    	correctionBox.prefWidthProperty().bind(correctionPane.widthProperty());
    	correctionBox.maxWidthProperty().bind(correctionPane.widthProperty());
    	correctionBox.minWidthProperty().bind(correctionPane.widthProperty());
    	
    	//zorgen dat de tekst wordt gewrapt op de juiste lengte
    	answerText.wrappingWidthProperty().bind(answerBox.widthProperty());
    	questionText.wrappingWidthProperty().bind(answerBox.widthProperty());
    	correctAnswerText.wrappingWidthProperty().bind(correctAnswerBox.widthProperty());
    }
    
    
    @FXML
    private void submitIncorrect() {
    	CorrectSubmit submit = new CorrectSubmit(answer.getAnswerId(),
    			jury.getUserId(), 0);
    	sendCorrection(submit);
    }
    
    @FXML
    private void submitCorrect() {
    	CorrectSubmit submit = new CorrectSubmit(answer.getAnswerId(),
    			jury.getUserId(),
    			answer.getMaxScore());
    	sendCorrection(submit);
    }
    
    private void sendCorrection(CorrectSubmit s) {
    	try {
			Client.getInstance().send(s);
			clear();
			refresh();
		} catch (IOException e) {
			messageMaker.showError("Problemen met de communicatie");
		}
    }
    
    @FXML
    private void refresh() {
    	//een antwoord aanvragen
    	try {
			AnswerRequest req = new AnswerRequest();
			req.setQuizId(quiz.getQuizId());
			req.onResponse(new FxResponseListener() {
				
				@Override
				public void handleFxResponse(Response response) {
					if(response instanceof ExceptionResponse) {
						messageMaker.showError(((ExceptionResponse) response).getExceptionMessage());
					} else if(response instanceof TimeOutResponse) {
						messageMaker.showInfo("Er zijn nog geen vragen\nom te verbeteren druk\nRefresh om opnieuw te proberen");
						refreshButton.setDisable(true);
					} else if(response instanceof AnswerResponse) {
						answer = (AnswerResponse) response;
						setToCorrect(answer);
					}		
				}
			});
			req.send();
			
		} catch (IdRangeException | IOException e) {
			messageMaker.showError("Problemen met communicatie met server");
		}
    }
    
    private void setToCorrect(AnswerResponse response) {
    	correctAnswerText.setText(response.getCorrectAnswer());
    	answerText.setText(response.getGivenAnswer());
    	questionText.setText(response.getQuestion());
    	
    	//de knoppen goed zetten
    	refreshButton.setDisable(true);
    	correctButton.setDisable(false);
    	wrongButton.setDisable(false);
    }
    
    //zet alles wat nodig is onzichtbaar
    private void clear() {
    	correctButton.setDisable(true);
    	wrongButton.setDisable(true);
    	correctionBox.setOpacity(0.0);
    }
    
    public void setJurry(LoginResponse jury) {
    	if(jury.getUserType().equals(UserType.JURY)) {
    		this.jury = jury;
    	} else {
    		throw new IllegalArgumentException("De megegeven gebruiker moet een jurry zijn");
    	}	
    	if(quiz!=null) {
    		refresh();
    	}
    }
    
   public void setQuiz(GetQuizResponse quiz) {
	   this.quiz = quiz;
	   if(jury!=null) {
		   refresh();
	   }
   }
    

}
