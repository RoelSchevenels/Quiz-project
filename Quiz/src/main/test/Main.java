/**
 * test javaFX applicatie
 */
package main.test;

import javaFXpanels.Backup.backupPane;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
	     Scene scene = new Scene(backupPane.getBackupPane());
	     stage.setScene(scene);
	     stage.sizeToScene();
	     stage.centerOnScreen();
	     stage.show();

	}

}
