package server.main;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.h2.command.dml.BackupCommand;
import org.hibernate.HibernateException;

import screenManger.ScreenManager;

import Protocol.RequestListener;
import Protocol.RequestManager;
import Protocol.SubmitManager;
import Protocol.requests.GetQuizRequest;
import Protocol.requests.Request;
import Protocol.responses.GetQuizResponse;
import Protocol.submits.Submit;
import Util.ConnectionUtil;
import Util.DatabaseUtil;
import javaFXpanels.Backup.BackupController;
import javaFXpanels.CreateQuiz.QuizMakerController;
import javaFXpanels.LoginServer.LoginPanelServer;
import javaFXpanels.MessageProvider.LoadingPane;
import javaFXpanels.MessageProvider.MessageProvider;
import javaFXpanels.Server.ServerController;
import javaFXtasks.StartServerTask;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import BussinesLayer.QuestionRound;
import BussinesLayer.Quiz;
import BussinesLayer.QuizMaster;
import GUI.quizMaster.QuizMasterPlayDisplay;

public class QuizMasterDisplay extends AnchorPane {
	private static URL LoginLocation = LoginPanelServer.class.getResource("LoginServer.fxml");
	private static URL choisePanel = ServerController.class.getResource("Server.fxml");
	private static URL createQuizLocation = QuizMakerController.class.getResource("quizMaker.fxml");
	private static URL createBackupLocation = BackupController.class.getResource("FXMLBackup.fxml");
	private MessageProvider messageMaker;
	private LoadingPane p;
	private QuizMaster quizMaster;
	
	public QuizMasterDisplay() {
		messageMaker = new MessageProvider(this);
		p = new LoadingPane();
		new JFXPanel(); //de javaFX thread starten
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				StartServer();
			}
		});
	}
		
	@SuppressWarnings("unchecked")
	private  void StartServer() {
		StartServerTask task = new StartServerTask();
		task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				p.hide();
				startLoginPane();
			}
		});
		task.setOnFailed(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {
				p.hide();
				messageMaker.showError("kon niet opstarten\nDe quiz app staat mischien\nOpen");
				
			}
		});
		
		ExecutorService ex = Executors.newCachedThreadPool();
		ex.execute(task);
		ex.shutdown();
		System.out.println("taak starten");
		p.showLoading("Opstarten...", this);
		
	}

	private void startLoginPane() {
		try {
			final LoginPanelServer controller = (LoginPanelServer) setFxml(LoginLocation);
			controller.setOnLoggedIn(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					quizMaster = controller.getQuizMaster();
					startChoiseMenu();
				}
			});
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void startChoiseMenu() {
		try {
			ServerController controlle = (ServerController) setFxml(choisePanel);
			controlle.setOnMakeBackup(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					startBackup();
				}
			});
			controlle.setOnMakeQuiz(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					startCreateQuiz();
				}
			});
		} catch (IOException e) {
			messageMaker.showError("Fatale fout");
		}
	}
	
	private void startCreateQuiz() {
		try {
			final QuizMakerController controller = (QuizMakerController) setFxml(createQuizLocation);
			controller.setQuizMaster(quizMaster);
			controller.setOnPlayQuiz(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					PlayQuiz(controller.getCurrentQuiz());
					
				}
			});
		} catch (IOException e) {
			messageMaker.showError("Fatale Fout");
		}
		
	}

	private void startBackup() {
		try {
			ConnectionUtil.CloseSessionFactory();
		} catch (HibernateException ex) {
			ex.printStackTrace();
		}
		try {
			setFxml(createBackupLocation);
		} catch (IOException e) {
			messageMaker.showError("Fatale fout");
		}
	}

	private void PlayQuiz(final Quiz q) {
		RequestManager.addRequestListener(GetQuizRequest.class, new RequestListener() {
			
			@Override
			public void handleRequest(Request r) {
				GetQuizResponse response = ((GetQuizRequest)r).createResponse();
				response.setQuizId(q.getQuizID());
				response.setQuizName(q.getQuizName());
				response.send();
			}
		});
		
		QuizMasterPlayDisplay display = new QuizMasterPlayDisplay(q);
		
		display.getCurrentRoundProperty().addListener(new ChangeListener<QuestionRound>() {

			@Override
			public void changed(
					ObservableValue<? extends QuestionRound> observable,
					QuestionRound oldValue, QuestionRound newValue) {
				DatabaseUtil.submitRound(q, newValue);
				
			}
		});
		
		//ScreenManager.getInstance().setFrameFullScreen("main", true);
		AnchorPane.setBottomAnchor(display, 0.0);
		AnchorPane.setTopAnchor(display, 0.0);
		AnchorPane.setRightAnchor(display, 0.0);
		AnchorPane.setLeftAnchor(display, 0.0);
		this.getChildren().setAll(display);
		
	}
	
	/**
	 * laad ieder soort pane van FXML en zet het als het huidige actieve paneel
	 * @param location
	 * @return
	 * @throws IOException
	 */
	private Initializable setFxml(URL location) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(location);
		Pane content = (Pane) loader.load();

		AnchorPane.setBottomAnchor(content, 0.0);
		AnchorPane.setTopAnchor(content, 0.0);
		AnchorPane.setRightAnchor(content, 0.0);
		AnchorPane.setLeftAnchor(content, 0.0);

		this.getChildren().setAll(content);

		return loader.getController();	
	}
}
