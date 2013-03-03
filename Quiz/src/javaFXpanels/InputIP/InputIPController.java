/**
 * Controller voor het ingeven van IP-adressen
 * 
 * @author De Meersman Vincent
 */

package javaFXpanels.InputIP;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import network.Client;
import javaFXpanels.MessageProvider.MessageProvider;
import javaFXtasks.StartClientTask;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
/**
 * @author Vincent
 * @author Roel
 */
public class InputIPController implements Initializable {
	@FXML
	private AnchorPane anchorPane;
	@FXML
	private AnchorPane progressAnchor;
	@FXML
	private AnchorPane inputAnchor;
	@FXML
	private ProgressIndicator loadingIndicator;
	@FXML
	private Button buttonConfirm;
	@FXML
	private TextField inputIP;
	@FXML
	private Label labelIP;
	
	private MessageProvider messageProvider;
	
	@SuppressWarnings("unused")
	private StartClientTask startClientTask;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		messageProvider = new MessageProvider(anchorPane);
	}
	
	@FXML
	private void confirmIPClicked(){
		try {
			Client.getInstance(inputIP.getText());
			//TODO: On finish: doorgaan naar volgende paneel
			//TODO: Bij doorgaan naar volgende paneel, ProgressIndicator verbergen
		}	catch (UnknownHostException uhe) {
			messageProvider.showError("Gelieve een geldig IP-adres in te geven.");
		} catch (IOException ioe) {
			messageProvider.showError("Kan geen verbinding maken met de server.");
		}
	}
	
	

}
