package javaFXpanels.MediaPane;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;

public interface MediaPaneController {
	public SimpleBooleanProperty getPlayPropertie();
	
	//boolean tussen 0 en 1 die het volume voorsteld
	public SimpleDoubleProperty getVolumePropertie();
	
	public void ReadyPlaying();
	
}
