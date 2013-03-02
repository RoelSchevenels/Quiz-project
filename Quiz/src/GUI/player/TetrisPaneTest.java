package GUI.player;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TetrisPaneTest extends Application {
	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage stage) 
	{
		TetrisPane pane = new TetrisPane();
		Scene scene = new Scene(pane,400,200);
		stage.setScene(scene);
		stage.show();
	}
}
