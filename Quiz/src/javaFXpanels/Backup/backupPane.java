package javaFXpanels.Backup;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class BackupPane {
	public static AnchorPane getBackupPane() throws IOException {
		return FXMLLoader.load(BackupController.class.getResource("FXMLBackup.fxml"));
	}
	
	//constructor onbruikbaar maken
	private BackupPane() {};
	
}
