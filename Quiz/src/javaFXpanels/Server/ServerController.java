package javaFXpanels.Server;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;



public class ServerController implements Initializable{
	
	@FXML
	private AnchorPane serverAnchor;
	@FXML
	private AnchorPane controlsAnchor;
	@FXML
	private Button maakQuiz;
	@FXML
	private Button speelQuiz;
	@FXML
	private Button maakBackup;
	@FXML
	private Label controlLabel;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}
	
	@FXML
	private void makeNewQuiz() {
		
	}
	
	@FXML
	private void playQuiz(){
		
	}
	
	@FXML
	private void makeBackup(){
		
	}

}
