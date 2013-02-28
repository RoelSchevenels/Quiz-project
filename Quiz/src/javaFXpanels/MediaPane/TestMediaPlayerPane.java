package javaFXpanels.MediaPane;

import java.io.File;

import BussinesLayer.resources.MediaResource;


import javafx.application.Application;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestMediaPlayerPane extends Application {
	final String TEST_MUSIC = "~/Desktop/02-MoneyForNothing.mp3".replaceFirst("~", System.getProperty("user.home"));
	final String TEST_MOVIE = "~/Desktop/Engrenages.S02E06.DVDrip.576p.H264.mp4".replaceFirst("~", System.getProperty("user.home"));
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		File f = new File(TEST_MOVIE);
		MediaResource r = new MediaResource(f);
		
		
		MediaPlayerPane player = new MediaPlayerPane();
		DefaultController d = player.getDefaultMediaController();
		
		Scene s1 = new Scene(player);
		primaryStage.setScene(s1);
		
		//2de stage
		Stage stage2 = new Stage();
		Scene s2 = new Scene(d);
		stage2.setScene(s2);
		
		
		stage2.show();
		primaryStage.show();
		primaryStage.setMinHeight(200);
		primaryStage.setMinWidth(200);
		
		player.setMovie(r);

	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
