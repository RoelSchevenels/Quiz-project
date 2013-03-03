/**
 * Loginpanel voor de Server-kant
 * 
 * @author De Meersman Vincent
 */
package javaFXpanels.LoginServer;

import java.net.URL;
import java.util.ResourceBundle;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javaFXpanels.MessageProvider.MessageProvider;
<<<<<<< HEAD

import BussinesLayer.QuizMaster;
import BussinesLayer.User;
import Protocol.responses.LoginResponse;
import Util.DatabaseUtil;

=======
>>>>>>> De quiz kan aangemaakt worden
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import BussinesLayer.QuizMaster;
import BussinesLayer.User;
import Protocol.responses.LoginResponse;
import Util.ConnectionUtil;
import Util.DatabaseUtil;

public class LoginPanelServer implements Initializable {
	@FXML
	private AnchorPane centerAnchor;
	@FXML
	private AnchorPane loginAnchor;
	@FXML
	private Pane registerPane;
	@FXML
	private Label labelGebruiker;
	@FXML
	private Label labelWachtwoord;
	@FXML
	private Label labelGeenGebruiker;
	@FXML
	private Label labelRegNaam;
	@FXML
	private Label labelRegVoornaam;
	@FXML
	private Label labelRegGebruikersnaam;
	@FXML
	private Label labelRegEmail;
	@FXML
	private Label labelRegWachtwoord;
	@FXML
	private Label labelRegWachtwoordOpnieuw;
	@FXML
	private Label labelRegType;
	@FXML
	private TextField fieldGebruiker;
	@FXML
	private PasswordField fieldWachtwoord;
	@FXML
	private TextField fieldRegNaam;
	@FXML
	private TextField fieldRegVoornaam;
	@FXML
	private TextField fieldRegGebruikersnaam;
	@FXML
	private TextField fieldRegEmail;
	@FXML
	private PasswordField fieldRegWachtwoord;
	@FXML
	private PasswordField fieldRegWachtwoordOpnieuw;
	@FXML
	private Button buttonGo;
	@FXML
	private Button buttonRegistreer;
	@FXML
	private Button buttonRegConfirm;
	@FXML
	private Button buttonRegAnnuleer;
	@FXML
	private RadioButton radioKwisser;
	@FXML
	private RadioButton radioJury;
	
	@SuppressWarnings("unused")
	private QuizMaster quizMaster;
	private EventHandler<ActionEvent> onLoggedIn;
	
	private MessageProvider messageProvider;
	private SimpleObjectProperty<LoginResponse> loginResponse;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		messageProvider = new MessageProvider(loginAnchor);
		loginResponse = new SimpleObjectProperty<LoginResponse>();
			
	}
	
	@FXML
	private void buttonGoClicked(){
		if (fieldGebruiker.getText().isEmpty() || fieldWachtwoord.getText().isEmpty()) {
			messageProvider.showWarning("Geen gebruikersnaam en/of wachtwoord ingevuld.");
		} else {
				User user = DatabaseUtil.getUser(fieldGebruiker.getText());
				if (user == null){
					messageProvider.showWarning("De gebruiker bestaat niet.");
				} else if (user instanceof QuizMaster) {
					if (user.checkPassword(fieldWachtwoord.getText())){
						setQuizMaster((QuizMaster) user);
						fireLoggedIn();
					} else {
						messageProvider.showError("Wachtwoord incorrect.");
					}
				} else {
					messageProvider.showInfo("Hier kunnen alleen Quizmasters inloggen.");
				}
		}
	}
	
	@FXML	
	private void buttonRegisterClicked(){
		centerAnchor.setVisible(false);
		centerAnchor.setDisable(true);
		registerPane.setVisible(true);
		registerPane.setDisable(false);
	}
	
	@FXML
	private void buttonRegGoClicked(){
		if (fieldRegGebruikersnaam.getText().isEmpty() || fieldRegWachtwoord.getText().isEmpty()
				|| fieldRegVoornaam.getText().isEmpty() || fieldRegNaam.getText().isEmpty() ||
				fieldRegEmail.getText().isEmpty() || fieldRegWachtwoordOpnieuw.getText().isEmpty()) {
			
			messageProvider.showWarning("Gelieve alle velden in te vullen.");
			
		} else if (!(fieldRegWachtwoord.getText().equals(fieldRegWachtwoordOpnieuw.getText()))) {
			
			messageProvider.showWarning("Wachtwoorden komen niet overeen.");
			
		} else {
			QuizMaster q = new QuizMaster(fieldRegGebruikersnaam.getText(), fieldRegWachtwoord.getText());
			q.setFirstName(fieldRegVoornaam.getText());
			q.setLastName(fieldRegNaam.getText());
			q.setEmail(fieldRegEmail.getText());
			
			Session s = ConnectionUtil.getSession();
			try {
				Transaction t = s.beginTransaction();
				s.save(q);
				t.commit();
				
			} catch(HibernateException ex) {
				messageProvider.showError("Kon gebruiker niet aanmaken");
			}
			s.close();
			
			fireLoggedIn();
		}
	}
	
	@FXML
	private void buttonRegAnnuleerClicked(){
		centerAnchor.setVisible(true);
		centerAnchor.setDisable(false);
		registerPane.setVisible(false);
		registerPane.setDisable(true);
	}
	
	public LoginResponse getLoginResponse() {
		return loginResponse.get();
	}

	public ReadOnlyObjectProperty<LoginResponse> loginResponseProperty() {
		return loginResponse;
	}

	public QuizMaster getQuizMaster() {
		return quizMaster;
	}

	public void setQuizMaster(QuizMaster quizMaster) {
		this.quizMaster = quizMaster;
	}
	
	public void setOnLoggedIn(EventHandler<ActionEvent> event) {
		this.onLoggedIn = event;
	}
	
	private void fireLoggedIn() {
		if(onLoggedIn != null) { 
			onLoggedIn.handle(new ActionEvent());
		}
	}
		
}
