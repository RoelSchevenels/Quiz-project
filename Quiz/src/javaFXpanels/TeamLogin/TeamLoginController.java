/**
 * voor een team in te loggen of aan te maken
 * @author vrolijkx
 */
package javaFXpanels.TeamLogin;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import javaFXpanels.MessageProvider.MessageProvider;

import Protocol.ResponseListener;
import Protocol.exceptions.IdRangeException;
import Protocol.requests.CreateUserRequest;
import Protocol.requests.GetTeamsRequest;
import Protocol.requests.LoginRequest;
import Protocol.requests.TeamLoginRequest;
import Protocol.responses.ExceptionResponse;
import Protocol.responses.GetTeamsResponse;
import Protocol.responses.LoginResponse;
import Protocol.responses.Response;
import Protocol.responses.TeamLoginResponse;
import Protocol.responses.LoginResponse.UserType;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;


/**
 * FXML Controller class
 *
 * @author Vrolijk Kristof <Vrolijkx.Kristof@gmail.com>
 */
public class TeamLoginController implements Initializable {
	@FXML
	private AnchorPane root;
	@FXML
	private AnchorPane loginAnchor;
	@FXML
	private AnchorPane teamLogin;
	@FXML
	private ScrollPane overFlowPane;
	@FXML
	private Accordion teamsAccordion;
	@FXML
	private PasswordField passwordField;
	@FXML
	private StackPane loadingPane;
	@FXML
	private StackPane noTeamsFoundPane;
	@FXML
	private Label loadingLabel;
	@FXML
	private Button addTeamButton;
	@FXML
	private Button loginButton;
	@FXML
	private AnchorPane makeTeamPane;
	@FXML
	private TextField teamNameField;
	@FXML
	private PasswordField paswordField;
	@FXML
	private PasswordField paswordField2;
	@FXML
	private ScrollPane playerListPane;
	@FXML
	private ListView<LoginResponse> playersList;
	@FXML
	private AnchorPane addUserPane;
	@FXML
	private Pane userLoginPane;
	@FXML
	private Pane userRegisterPane;
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
	private RadioButton radioKwisser;
	@FXML
	private RadioButton radioJury;
	
	private MessageProvider messageMaker;
	
	//niet Fxml
	private SimpleIntegerProperty playerId;
	private SimpleObjectProperty<TeamLoginResponse> teamResponse;
	private HashMap<TitledPane, Integer> paneForTeam;
	private int currentTeam;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
		messageMaker = new MessageProvider(root);
    	playerId = new SimpleIntegerProperty();
    	teamResponse = new SimpleObjectProperty<TeamLoginResponse>();
    	paneForTeam = new HashMap<TitledPane, Integer>();
    	
		initBindings();
		showLoading("Teams ophalen...");
		root.getChildren().remove(loginAnchor);
		loginAnchor.setVisible(true);
		
		AnchorPane.setTopAnchor(loginAnchor, 0.0);
		AnchorPane.setRightAnchor(loginAnchor, 0.0);
		AnchorPane.setLeftAnchor(loginAnchor, 0.0);
		
		//TODO: remove test;
		test();
		
		
    } 
    private void test() {
    	GetTeamsResponse r = new GetTeamsResponse(20);
    	r.addTeamItem("feesthonde", 34);
    	r.addTeamItem("beestig goed", 78);
    	
    	setReceivedTeams(r);
    	loadingPane.setVisible(false);
    }
    
    public void initBindings() {
    	playersList.prefWidthProperty().bind(playerListPane.widthProperty());
    	
    	teamsAccordion.minWidthProperty().bind(overFlowPane.widthProperty().add(-5));
    	teamsAccordion.expandedPaneProperty().addListener(new ChangeListener<TitledPane>() {
			@Override
			public void changed(ObservableValue<? extends TitledPane> observable,
					TitledPane oldValue, TitledPane newValue) {
				if(newValue != null) {
					AnchorPane p = (AnchorPane)newValue.getContent();
					currentTeam = paneForTeam.get(newValue);
					if(!p.getChildren().contains(loginAnchor)) {
						p.getChildren().add(loginAnchor);
					}
				}
			}
    		
		});
    	
    	
    	//deze binding kijkt of de teamsAccordian onderdelen bevat
    	BooleanBinding hasContent = Bindings.createBooleanBinding(new Callable<Boolean>() {
			
			@Override
			public Boolean call() throws Exception {
				if(teamsAccordion.getChildrenUnmodifiable().size() > 0) {
					return true;
				} else {
					return false;
				}
			}
		}, teamsAccordion.getChildrenUnmodifiable());
    	
    	noTeamsFoundPane.visibleProperty().bind(
    			loadingPane.visibleProperty().not()
    			.and(hasContent.not()));
    }
    
    public void setplayerId(int playerId) {
    	this.playerId.set(playerId);
    	try {
			GetTeamsRequest request = new GetTeamsRequest(playerId);
			
			request.onResponse(new ResponseListener() {	
				@Override
				public void handleResponse(Response response) {
					loadingPane.setVisible(false);
					if(response instanceof ExceptionResponse) {
						messageMaker.showError(((ExceptionResponse) response).getExceptionMessage());
						return;
					} else if(response instanceof GetTeamsResponse) {
						setReceivedTeams((GetTeamsResponse) response);
					}
					
					
					addTeamButton.setDisable(false);
				}
			});
			
		} catch (IdRangeException e) {
			loadingPane.setVisible(false);
			messageMaker.showError("Kon niet naar de server verzenden");
		}
    	
    }
    
    private void switchActivePane(Pane oldPane,
			Pane newPane) {
		oldPane.setVisible(false);
		oldPane.setDisable(true);
		newPane.setVisible(true);
		newPane.setDisable(false);
		
	}
    
    private void setReceivedTeams(GetTeamsResponse response) {
    	teamsAccordion.getPanes().clear();
    	
    	HashMap<Integer, String> vars = response.getTeamItems();
    	
    	for(int i: vars.keySet()) {
    		
    		TitledPane t = new TitledPane(vars.get(i),getEmptyLoginPane());
    		paneForTeam.put(t, i);
    		teamsAccordion.getPanes().add(t);
    	}
    }
    
    private AnchorPane getEmptyLoginPane() {
    	AnchorPane a = new AnchorPane();
    	a.setPrefHeight(loginAnchor.getPrefHeight());
    	a.setMaxHeight(loginAnchor.getMaxHeight());
    	return a;
    }
    
    private void showLoading(String reason) {
    	loadingLabel.setText(reason);
    	loadingPane.setVisible(true);
    }

	@FXML
    private void createTeamPressed() {
    	//TODO: CreatTeamRequestAanmaken
    }
    
    @FXML
    private void addTeamPressed() {
    	switchActivePane(teamLogin, makeTeamPane);
    }
    
    @FXML
    private void loginPressed() {
    	if(passwordField.getText().isEmpty()) {
    		messageMaker.showWarning("Gelieve een wachtwoord in te geven");
    		return;
    	} 
    	
    	try {
			TeamLoginRequest request = new TeamLoginRequest(currentTeam,passwordField.getText());
			request.onResponse(new ResponseListener() {
				@Override
				public void handleResponse(Response response) {
					if(response instanceof ExceptionResponse) {
						messageMaker.showError(((ExceptionResponse) response).getExceptionMessage());
					} else if(response instanceof TeamLoginResponse) {
						teamResponse.set((TeamLoginResponse) response);
					}
					loadingPane.setVisible(false);
				}
			});
			request.send();
			showLoading("Inloggen op de server...");
		} catch (IdRangeException e) {
			messageMaker.showError("Kon pakket niet doorsturen");
		} catch (Exception ex) {
			messageMaker.showError("fout bij versturen");
		}
    }

    @FXML
    private void addUserPressed() {
    	switchActivePane(makeTeamPane,addUserPane);
    }
     
    @FXML
    private void userLoginPressed() {
    	try {
			new LoginRequest(fieldGebruiker.getText(), fieldWachtwoord.getText());
			//TODO: versturen naar de server van de login
			
		} catch (IdRangeException e) {
				messageMaker.showError("Fout bij het aanmelden.");
		}
    }
    
    @FXML
    private void registerUserPressed() {
    	
    }
    
    @FXML
    private void registerCompletePressed() {
    	if (fieldRegGebruikersnaam.getText().isEmpty() || fieldRegWachtwoord.getText().isEmpty()
				|| fieldRegVoornaam.getText().isEmpty() || fieldRegNaam.getText().isEmpty() ||
				fieldRegEmail.getText().isEmpty() || fieldRegWachtwoordOpnieuw.getText().isEmpty()) {
			
			messageMaker.showWarning("Gelieve alle velden in te vullen.");
			
		} else if (!(fieldRegWachtwoord.getText().equals(fieldRegWachtwoordOpnieuw.getText()))) {
			
			messageMaker.showWarning("Wachtwoorden komen niet overeen.");
			
		} else {
	
			UserType t = null;
			if (radioJury.isSelected()) {
				t = UserType.JURRY;
			} else if (radioKwisser.isSelected()) {
				t = UserType.PLAYER;
			}
			try {
				CreateUserRequest r = new CreateUserRequest(fieldRegGebruikersnaam.getText(),
						fieldRegWachtwoord.getText(),
						fieldRegVoornaam.getText(), fieldRegNaam.getText(),
						fieldRegEmail.getText(), t);
				
				//TODO: versturen en voor de response luisteren
			} catch (IdRangeException e) {
				messageMaker.showError("Fout bij communicatie\nmet de server");
			}
		}
    }
    
    @FXML
    private void regiserCanceledPressed() {
    	switchActivePane(userRegisterPane, userLoginPane);
    }
    
    @FXML
    private void createAndLoginPressed() {
    	//TODO: CreateTeamResponseAanmaken
    }
    
	public SimpleObjectProperty<TeamLoginResponse> teamResponseProperty() {
		return teamResponse;
	}

	public TeamLoginResponse getTeamResponse() {
		return teamResponse.get();
	}
}
