package server.main;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import javaFXpanels.MessageProvider.LoadingPane;
import javaFXpanels.MessageProvider.MessageProvider;
import javaFXtasks.StartServerTask;

import javafx.application.Platform;
import javafx.concurrent.WorkerStateEvent;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class QuizMasterDisplay extends AnchorPane {
	private MessageProvider messageMaker;
	private LoadingPane p;
	
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




	protected void startLoginPane() {
		// TODO Auto-generated method stub
		
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
