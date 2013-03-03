/**
 * Controller klasse voor de GUI op de server-kant
 * 
 * @author De Meersman Vincent
 */

package javaFXpanels.Server;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;



public class ServerController implements Initializable{	
	@FXML
	private AnchorPane serverAnchor;
	private EventHandler<ActionEvent> onPlayQuiz;
	private EventHandler<ActionEvent> onMakeQuiz;
	private EventHandler<ActionEvent> onMakeBackup;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}
	
	@FXML
	private void makeNewQuiz() {
		if(this.onMakeQuiz != null) {
			onMakeQuiz.handle(new ActionEvent());
		} 
	}
	
	@FXML
	private void playQuiz(){
		if(this.onPlayQuiz != null) {
			onPlayQuiz.handle(new ActionEvent());
		} 
	}
	
	@FXML
	private void makeBackup(){
		if(this.onMakeBackup != null) {
			onMakeBackup.handle(new ActionEvent());
		} 
	}

	public void setOnPlayQuiz(EventHandler<ActionEvent> onPlayQuiz) {
		this.onPlayQuiz = onPlayQuiz;
	}

	public void setOnMakeQuiz(EventHandler<ActionEvent> onMakeQuiz) {
		this.onMakeQuiz = onMakeQuiz;
	}

	public void setOnMakeBackup(EventHandler<ActionEvent> onMakeBackup) {
		this.onMakeBackup = onMakeBackup;
	}
	
	

}
