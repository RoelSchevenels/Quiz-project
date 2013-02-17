/**
 * Een FXML controller voor de mediaKnoppen
 * @author vrolijkx
 */
package javaFXpanels.MediaPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Vrolijk Kristof <Vrolijkx.Kristof@gmail.com>
 */
public class MediaControllerController implements Initializable, MediaPaneController {
	@FXML
	private AnchorPane container;
	@FXML
	private ImageView playImage;
	@FXML
	private ImageView pauzeImage;
	@FXML
	private ImageView muteImage;
	@FXML
	private ImageView lowerSoundImage;
	@FXML
	private ImageView upperSoundImage;
	
	
	//voor de controller zelf
	private SimpleBooleanProperty playProperty;
	private SimpleDoubleProperty volumeProperty;
	private boolean mute;
	private double beforeMute;
	
	
    /**
     * Initializes the controller class.
     */
    @Override
      public void initialize(URL url, ResourceBundle rb) {
    	playProperty = new SimpleBooleanProperty(false);
    	volumeProperty = new SimpleDoubleProperty(0.7);
    }

	@Override
	public SimpleBooleanProperty getPlayPropertie() {
		return playProperty;
	}

	@Override
	public SimpleDoubleProperty getVolumePropertie() {
		return volumeProperty;
	}

	@Override
	public void ReadyPlaying() {
		if(this.playProperty.get()) {
			this.playPauze();
		}	
	}   
	
	@FXML
	private void mute() {
		if(this.mute && beforeMute >= 0.1) {
			this.muteImage.setOpacity(1.0);
			this.volumeProperty.setValue(this.beforeMute);
			this.mute = false;
		} else {
			this.beforeMute = this.volumeProperty.get();
			this.volumeProperty.set(0.0);
			this.upperSoundImage.setOpacity(1.0);
			this.muteImage.setOpacity(0.5);
			this.mute = true;
		}
	}
	
	@FXML 
	private void playPauze() {
		if(this.playProperty.get()) {
			this.playProperty.set(false);
			this.playImage.setVisible(true);
			this.pauzeImage.setVisible(false);
		} else {
			this.playProperty.set(true);
			this.playImage.setVisible(false);
			this.pauzeImage.setVisible(true);
		}
	}
	
	@FXML
	private void soundUp() {
		if(mute) {
			mute();
		}
		double vol = this.volumeProperty.get() + 0.1;
		vol = vol < 1 ? vol : 1;
		this.volumeProperty.set(vol);
		this.muteImage.setOpacity(1);
		this.lowerSoundImage.setOpacity(1);
		
		if(vol >= 1) {
			upperSoundImage.setOpacity(0.5);
		}
		
	}
	
	@FXML
	private void soundDown() {
		if(this.mute) {
			mute();
		}
		double vol = this.volumeProperty.get() - 0.1;
		vol = vol > 0 ? vol : 0;
		this.volumeProperty.set(vol);
		this.upperSoundImage.setOpacity(1);
		if(vol == 0) {
			mute();
			lowerSoundImage.setOpacity(0.5);
			
		}
	}

	@FXML 
	private void imageEnter(Event e) {
		Node source = (Node) e.getSource();
		source.setEffect(new Glow());
	}
	
	@FXML 
	private void imageExit(Event e) {
		Node source = (Node) e.getSource();
		source.setEffect(null);
	}
}
