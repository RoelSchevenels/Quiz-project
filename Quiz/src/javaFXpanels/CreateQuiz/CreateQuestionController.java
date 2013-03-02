/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javaFXpanels.CreateQuiz;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author Vrolijk Kristof <Vrolijkx.Kristof@gmail.com>
 */
public class CreateQuestionController implements Initializable {
	private FileChooser fileChooser;
	@FXML
	private AnchorPane root;
	@FXML
	private VBox multipleChoiseBox;
	@FXML
	private TextArea openQuestionText;
	@FXML
	private TextArea openAnswerText;
	@FXML
	private TextArea multipleQuestionText;
	@FXML
	private TextField multiplePosibilityText;
	@FXML
	private VBox mutlipleChoiseBox;
	@FXML
	private TextArea mediaQuestionText;
	@FXML
	private TextArea mediaAnswerText;
	@FXML
	private Label fileLabel;
	@FXML
	private Button openAcceptButton;
	@FXML
	private Button multipleAcceptButton;
	@FXML
	private Button mediaAcceptButton;
	@FXML
	private Button multipleAddButton;
	

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	fileChooser = new FileChooser();
        initBindings();
    }
    
    private void initBindings() {
		openQuestionText.textProperty()
			.isNotEqualTo("")
			.and(openAnswerText.textProperty()
					.isNotEqualTo(""))
			.addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(
						ObservableValue<? extends Boolean> observable,
						Boolean oldValue, Boolean newValue) {
					if(newValue) {
						openAcceptButton.setDisable(false);
					} else {
						openAcceptButton.setDisable(true);
					}
				}
			});
		
		mediaQuestionText.textProperty()
			.isNotEqualTo("")
			.and(mediaAnswerText.textProperty()
				.isNotEqualTo(""))
			.addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(
					ObservableValue<? extends Boolean> observable,
					Boolean oldValue, Boolean newValue) {
					if(newValue) {
						mediaAcceptButton.setDisable(false);
					} else {
						mediaAcceptButton.setDisable(true);
					}
				}
			});
	}

	@FXML
    private void cancel() {
    	
    }
}
