/**
 * voor een team in te loggen of aan te maken
 * @author vrolijkx
 */
package javaFXpanels.TeamLogin;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import javaFXpanels.MessageProvider.MessageProvider;

import Protocol.ResponseListener;
import Protocol.exceptions.IdRangeException;
import Protocol.requests.CreateTeamRequest;
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
	private AnchorPane teamLoginPane;
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
	private PasswordField createPasswordField;
	@FXML
	private PasswordField createPaswordField2;
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
	private SimpleObjectProperty<LoginResponse> playerResponse;
	private SimpleObjectProperty<TeamLoginResponse> teamResponse;
	private HashMap<TitledPane, Integer> paneForTeam;
	private int currentTeam;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
		messageMaker = new MessageProvider(root);
    	playerResponse = new SimpleObjectProperty<LoginResponse>();
    	teamResponse = new SimpleObjectProperty<TeamLoginResponse>();
    	paneForTeam = new HashMap<TitledPane, Integer>();
    	
		initBindings();
		showLoading("Teams ophalen...");
		root.getChildren().remove(loginAnchor);
		loginAnchor.setVisible(true);
		
		AnchorPane.setTopAnchor(loginAnchor, 0.0);
		AnchorPane.setRightAnchor(loginAnchor, 0.0);
		AnchorPane.setLeftAnchor(loginAnchor, 0.0);
		
		
    } 
    
    
    private void initBindings() {
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
    	loadingPane.requestFocus();
    }

	@FXML
    private void createTeamPressed() {
		try {
			CreateTeamRequest request = creatTeamLoginRequest();
			if(request == null) {
				return;
			}
			
			request.onResponse(new ResponseListener() {
				@Override
				public void handleResponse(Response response) {
					if(response instanceof ExceptionResponse) {
						messageMaker.showError(((ExceptionResponse) response).getExceptionMessage());
					} else if(response instanceof TeamLoginResponse) {
						switchActivePane(makeTeamPane, teamLoginPane);
						setplayerLoginResponse(playerResponse.get());
						playersList.getItems().setAll(playerResponse.get());
						loadingPane.setVisible(false);
					}
					
				}
			});
			
			request.send();
			showLoading("Team aanmelden...");
			
		} catch (IdRangeException | IOException e) {
			messageMaker.showError("problemen met de database");
		}
    }

	@FXML
	private void createTeamAndLoginPressed() {
		try {
			CreateTeamRequest request = creatTeamLoginRequest();
			if(request == null) {
				return;
			}
			
			request.onResponse(new ResponseListener() {
				@Override
				public void handleResponse(Response response) {
					if(response instanceof ExceptionResponse) {
						messageMaker.showError(((ExceptionResponse) response).getExceptionMessage());
					} else if(response instanceof TeamLoginResponse) {
						teamResponse.set((TeamLoginResponse) response);
						loadingPane.setVisible(false);
					}
					
				}
			});
			
			request.send();
			showLoading("Team aanmelden...");
			
		} catch (IdRangeException | IOException e) {
			messageMaker.showError("problemen met de database");
		}
	}
	
    private CreateTeamRequest creatTeamLoginRequest() throws UnknownHostException, IdRangeException, IOException {
    	if(teamNameField.getText().isEmpty() 
    			|| !createPasswordField.getText().isEmpty()
    			|| !createPaswordField2.getText().isEmpty()) {
    		messageMaker.showWarning("Gelieve alle velden in te vullen");
    		return null;
    	} else if(!createPasswordField.getText().equals(createPaswordField2.getText())) {
    		messageMaker.showWarning("De wachtwoorden komen niet overeen");
    		return null;
    	}
    	
    	CreateTeamRequest request = new CreateTeamRequest();
    	request.setTeamName(teamNameField.getText());
    	request.setPassword(createPasswordField.getText());
    	for(LoginResponse r: playersList.getItems()) {
    		request.addPlayer(r.getUserId());
    	}
    	
    	return request;
    }
    
    @FXML
    private void addTeamPressed() {
    	switchActivePane(teamLoginPane, makeTeamPane);
    	playersList.getItems().clear();
    	//TODO: de huide user toevoegen aan de players pane
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
			LoginRequest request = new LoginRequest(fieldGebruiker.getText(), fieldWachtwoord.getText());
			request.onResponse(new ResponseListener() {
				
				@Override
				public void handleResponse(Response response) {
					if(response instanceof ExceptionResponse) {
						messageMaker.showError(((ExceptionResponse) response).getExceptionMessage());
					} else if(response instanceof LoginResponse) {
						playersList.getItems().add((LoginResponse) response);
						switchActivePane(addUserPane, makeTeamPane);
					}
					loadingPane.setVisible(false);				
				}
			});
			
			
			request.send();
			showLoading("User aanmelden...");
		} catch (IdRangeException e) {
			messageMaker.showError("Fout bij het aanmelden.");
		} catch (UnknownHostException e) {
			messageMaker.showError("Fout bij het aanmelden.");
		} catch (IOException e) {
			messageMaker.showError("Fout bij het aanmelden.");
		}
    }

    @FXML
    private void cancelUserLogin() {
    	switchActivePane(addUserPane, makeTeamPane);
    }
    
    @FXML
    private void registerUserPressed() {
    	switchActivePane(userLoginPane, userRegisterPane);
    }
    
    @FXML
    private void registerCancelPressed() {
    	switchActivePane(userRegisterPane, userLoginPane);
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
				t = UserType.JURY;
			} else if (radioKwisser.isSelected()) {
				t = UserType.PLAYER;
			}
			try {
				CreateUserRequest r = new CreateUserRequest(fieldRegGebruikersnaam.getText(),
						fieldRegWachtwoord.getText(),
						fieldRegVoornaam.getText(), fieldRegNaam.getText(),
						fieldRegEmail.getText(), t);
				
				r.onResponse(new ResponseListener() {
					@Override
					public void handleResponse(Response response) {
						if(response instanceof ExceptionResponse) {
							messageMaker.showError(((ExceptionResponse) response).getExceptionMessage());
						} else if(response instanceof LoginResponse) {
							playersList.getItems().add((LoginResponse) response);
							switchActivePane(addUserPane, makeTeamPane);
						}
						loadingPane.setVisible(false);		
					}
				});
				
			} catch (IdRangeException e) {
				messageMaker.showError("Fout bij communicatie\nmet de server");
			} catch (UnknownHostException e) {
				messageMaker.showError("Fout bij communicatie\nmet de server");
			} catch (IOException e) {
				messageMaker.showError("Fout bij communicatie\nmet de server");
			}
		}
    }
    
	public SimpleObjectProperty<TeamLoginResponse> teamResponseProperty() {
		return teamResponse;
	}

	public TeamLoginResponse getTeamResponse() {
		return teamResponse.get();
	}
	
	public void setplayerLoginResponse(LoginResponse response) {
    	if(!response.getUserType().equals(UserType.PLAYER)) {
    		throw new IllegalArgumentException("the response must be one of a player");
    	}
    	
 
    	this.playersList.getItems().add(response);
    	this.playerResponse.set(response);
    	try {
			GetTeamsRequest request = new GetTeamsRequest(response.getUserId());
			
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
			messageMaker.showError("Kon pakket niet doorsturen");
		} catch (Exception ex) {
			messageMaker.showError("fout bij versturen");
		}
	}
			
}
