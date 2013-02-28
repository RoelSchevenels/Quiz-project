/** 
 * Controller om vragen te weergeven
 * 
 * @author De Meersman Vincent	
 */
package javaFXpanels.Questions;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class QuestionDisplayController implements Initializable{
	@FXML
	private AnchorPane questionAnchor;
	@FXML
	private AnchorPane openQuestionAnchor;
	@FXML
	private AnchorPane multipleChoiceAnchor;
	@FXML
	private AnchorPane imageQuestionAnchor;
	@FXML
	private Text openQuestionText;
	@FXML
	private Text multipleChoiceText;
	@FXML
	private Text imageQuestionText;
	@FXML
	private Label openQuestionOplossingLabel;
	@FXML
	private Label multipleChoiceOplossingLabel;
	@FXML
	private Label imageQuestionOplossingLabel;
	@FXML
	private TextField openAnswer;
	@FXML
	private TextField imageAnswer;
	@FXML
	private Button openConfirm;
	@FXML
	private Button multipleChoiceConfirm;
	@FXML
	private Button imageConfirm;
	@FXML
	private VBox multipleChoiceVBox;
	@FXML
	private ImageView imageQuestionView;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
			
	}

}
