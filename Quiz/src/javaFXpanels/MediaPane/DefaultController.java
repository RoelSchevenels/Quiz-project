/**
 * een heel simpele mediaPane Controller moet liefst
 * liefst vervangen worden door 1 met mooie gui.
 * @author vrolijkx
 */
package javaFXpanels.MediaPane;


import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;

public class DefaultController extends FlowPane implements MediaPaneController {
	private SimpleBooleanProperty playProperty;
	private SimpleDoubleProperty  volumeProperty;
	
	public DefaultController() {
		playProperty = new SimpleBooleanProperty(false);
		volumeProperty = new SimpleDoubleProperty(1.0);
		
		intitCompontent();
	}
	
	private void intitCompontent() {
		Button b = new Button("Play/Pauze");
		b.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				toglePlay();
			}
		});
		this.getChildren().add(b);
	}

	@Override
	public SimpleBooleanProperty getPlayPropertie() {
		return playProperty;
	}

	@Override
	public SimpleDoubleProperty getVolumePropertie() {
		return volumeProperty;
	}

	private void toglePlay() {
		if(playProperty.get()) {
			playProperty.set(false);
		} else {
			playProperty.set(true);
		}
	}

	@Override
	public void ReadyPlaying() {
		this.playProperty.set(false);
	};
}
