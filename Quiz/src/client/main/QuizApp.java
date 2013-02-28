package client.main;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import network.Client;

import Protocol.responses.LoginResponse;
import Protocol.responses.LoginResponse.UserType;
import Protocol.responses.TeamLoginResponse;
import javaFXpanels.Login.LoginPanel;
import javaFXpanels.Questions.QuestionDisplayController;
import javaFXpanels.TeamLogin.TeamLoginController;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class QuizApp extends Application {
	private static URL loginLocation;
	private static URL teamLoginLocation;
	private static URL quizLocation;
	private Stage mainStage;
	private AnchorPane root;
	private LoginResponse login;
	private FXMLLoader loader = new FXMLLoader();
	
	static {
		loginLocation = LoginPanel.class.getResource("Login.fxml");
		teamLoginLocation = TeamLoginController.class.getResource("teamLogin.fxml");
		quizLocation = QuestionDisplayController.class.getResource("QuestionDisplay.fxml");
	}
	
	@Override
	public void start(Stage primaryStage) {
		root = new AnchorPane();
		mainStage = primaryStage;
		mainStage.setMinHeight(600.0);
		mainStage.setMinWidth(700.0);
		mainStage.setScene(new Scene(root));
		mainStage.show();
				
		startDetectServer();
		startLoginPane();
	}
	
	private void startDetectServer() {
		ExecutorService ex = Executors.newCachedThreadPool();
		try {
			ex.execute(Client.getInstance());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * loads every kind of pane from fxml
	 * @param location
	 * @return
	 * @throws IOException
	 */
	private Initializable setFxml(URL location) throws IOException {
		loader.setLocation(location);
		Pane content = (Pane) loader.load();
		
		AnchorPane.setBottomAnchor(content, 0.0);
		AnchorPane.setTopAnchor(content, 0.0);
		AnchorPane.setRightAnchor(content, 0.0);
		AnchorPane.setLeftAnchor(content, 0.0);
		
		root.getChildren().setAll(content);
		
		return loader.getController();
		
	}
	
	private void startLoginPane() {
		try {
			LoginPanel controller = (LoginPanel) setFxml(loginLocation);

			controller.loginResponseProperty().addListener(new ChangeListener<LoginResponse>() {
				@Override
				public void changed(
						ObservableValue<? extends LoginResponse> observable,
						LoginResponse oldValue, LoginResponse newValue) {
					if(newValue!= null) {
						login = newValue;
						if(newValue.getUserType().equals(UserType.JURY)) {
							startJurry(newValue);
						} else if(newValue.getUserType().equals(UserType.PLAYER)) {
							startTeamLoginPane(newValue);
						}
					}
				}
			});
			// en er is ingelogd
			
		} catch (IOException e) {
			//fatale fout niet te recepureren
			e.printStackTrace();
			System.exit(99999);
		}
	}
	
	private void startJurry(LoginResponse newValue) {
		// TODO Auto-generated method stub
		
	}

	private void startTeamLoginPane(LoginResponse r) {
		
		try {
			TeamLoginController controller = (TeamLoginController) setFxml(teamLoginLocation);
		
			controller.setplayerLoginResponse(r);
			controller.teamResponseProperty().addListener(new ChangeListener<TeamLoginResponse>() {
				@Override
				public void changed(
						ObservableValue<? extends TeamLoginResponse> observable,
						TeamLoginResponse oldValue, TeamLoginResponse newValue) {
					if(newValue != null) {
						connectToQuiz();
					}
				}
			});
			
		} catch (IOException e) {
			//fatale fout niet te recepureren
			e.printStackTrace();
			System.exit(99999);
		}
	}
	
	
	public void connectToQuiz() {
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
