package javaFXpanels.MediaPane;

import java.io.File;

import BuisinesLayer.resources.MediaResource;


import javafx.application.Application;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class tesMediaPlayerPane extends Application {
	final String TEST_MUSIC = "/Users/vrolijkx/Desktop/02-MoneyForNothing.mp3";
	final String TEST_MOVIE = "/Users/vrolijkx/Desktop/Engrenages.S02E06.DVDrip.576p.H264.mp4";
	
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
