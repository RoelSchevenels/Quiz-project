/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javaFXpanels.Jurry;

import java.net.URL;
import java.util.ResourceBundle;

import Protocol.AnswerResponse;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

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
    private int AnswerId;
    private int jurryId;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
    private void SubmitIncorrect() {
    	
    	clear();
    }
    
    @FXML
    private void SubmitCorrect() {
    	
    	clear();
    }
    
    public void setQuestion(AnswerResponse response) {
    	
    }
    
    //zet alles wat nodig is onzichtbaar
    private void clear() {
    	correctButton.setDisable(true);
    	wrongButton.setDisable(true);
    	correctionBox.setOpacity(0.0);
    }
}
