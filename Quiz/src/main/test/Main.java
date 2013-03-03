/**
 * test javaFX applicatie
 */
package main.test;

import javaFXpanels.Backup.BackupPane;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
	     Scene scene = new Scene(BackupPane.getBackupPane());
	     stage.setScene(scene);
	     stage.sizeToScene();
	     stage.centerOnScreen();
	     stage.show();

	}

}
