/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javaFXpanels.Jurry;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
}
