package javaFXpanels.Backup;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class backupPane {
	public static AnchorPane getBackupPane() throws IOException {
		return FXMLLoader.load(FXMLBackupController.class.getResource("FXMLBackup.fxml"));
	}
	
	//constructor onbruikbaar maken
	private backupPane() {};
	
}
