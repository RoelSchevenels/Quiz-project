/**
 * een pane om Video en audio weer te geven
 * Te gebruiken met exteren controllers
 * @author vrolijkx
 */
package javaFXpanels.MediaPane;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import org.hibernate.mapping.Set;

import BussinesLayer.resources.MediaResource;


import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.stage.Window;

public class MediaPlayerPane extends AnchorPane {
	private ArrayList<Node> childeren;
	private ChangeListener<Number> sizeListener;
	private ChangeListener<Boolean> playListener;
	private MediaPlayer player;
	private MediaPaneController controller;
	private MediaView mediaView;
	private Boolean autoPlay;
	private Media media;


	public MediaPlayerPane() {
		this.setStyle("-fx-background-color: rgb(0,0,0);");
		childeren = new ArrayList<Node>();
		autoPlay = false;
		//changelistener voor resize van container
		sizeListener = new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				scaleMovie();
			}
		};

		playListener = new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
				setPlay(ov.getValue());
			}
		};
	}

	private void setPlay(Boolean b) {
		if(player != null) {
			if(b) {
				player.play();
			} else {
				player.pause();
			}
		}
	}
	
	private void initMovie() {
		if(this.mediaView == null) {
			mediaView = new MediaView();
			
		}

		mediaView.setMediaPlayer(player);
		this.setPrefSize(media.getWidth(), media.getHeight());


		//voorkomen van dubbel toevoegen
		this.heightProperty().removeListener(sizeListener);
		this.widthProperty().removeListener(sizeListener);

		//size listener toevoegen
		this.heightProperty().addListener(sizeListener);
		this.widthProperty().addListener(sizeListener);

		ArrayList<Node> childeren = new ArrayList<Node>(this.getChildren());
		childeren.remove(mediaView); 
		
		if(!this.getChildren().contains(mediaView)) {
			this.getChildren().add(mediaView);
		}
		scaleMovie();

	};

	private void scaleMovie() {
		if(this.getHeight() == 0 || this.getWidth() == 0) {
			//scherm zonder afmeeting moet niet geschaald worden
			return;
		}
		
		double scaleX = this.getWidth()/media.getWidth();
		double scaleY = this.getHeight()/media.getHeight();

		//de te gebruiken scale kiezen
		double scale = scaleX < scaleY ? scaleX : scaleY;

		double leftAndRightAnchor = (this.getWidth() -  media.getWidth())/2;
		double topAndBottomAnchor = (this.getHeight() - media.getHeight())/2;

		AnchorPane.setLeftAnchor(mediaView, leftAndRightAnchor);
		AnchorPane.setRightAnchor(mediaView, leftAndRightAnchor);
		AnchorPane.setTopAnchor(mediaView, topAndBottomAnchor);
		AnchorPane.setBottomAnchor(mediaView, topAndBottomAnchor);

		mediaView.setScaleX(scale);
		mediaView.setScaleY(scale);

	}

	private void initSound() {
		//TODO:de sound animatie toevoegen

	};

	public void SetAudio(MediaResource m) {
		setMedia(m.getMedia());

		player.setOnReady(new Runnable() {

			@Override
			public void run() {
				initSound();
				if(autoPlay) {
					player.play();
				}
			}
		});
	}

	public void setMovie(MediaResource m) {
		setMedia(m.getMedia());
		
		player.setOnReady(new Runnable() {

			@Override
			public void run() {
				initMovie();
				if(autoPlay) {
					player.play();
				}
			}
		});
	}

	private void setMedia(Media m) {
		media = m;
		player = new MediaPlayer(m);
		setController(controller);

		player.setOnEndOfMedia(new Runnable() {

			@Override
			public void run() {
				controller.ReadyPlaying();
				player.stop();

			}
		});
	}

	public DefaultController getDefaultMediaController() {
		DefaultController c = new DefaultController();
		this.setController(c);
		return c;
	}
	
	public void setController(MediaPaneController c) {
		removeController();
		this.controller = c;
		if(c!= null) {
			setPlay(controller.getPlayPropertie().getValue());
			controller.getPlayPropertie().addListener(playListener);
			setPlay(controller.getPlayPropertie().getValue());
			
			if(player != null) {
				player.volumeProperty().bind(c.getVolumePropertie());
			}
		}
	}

	private void removeController(){
		if(controller != null) {
			//playlistener verwijderen
			controller.getPlayPropertie().removeListener(playListener);
			
			if(player != null) {
				player.volumeProperty().unbind();
			}
		}
	}

	public Boolean getAutoPlay() {
		return autoPlay;
	}

	public void setAutoPlay(Boolean autoPlay) {
		this.autoPlay = autoPlay;
	}

	//aanroepen om de video van het scherm te clearen
	//en om content terug weer te geven
	public void clearPlayer() { 
		removeController();
		//het cherm even verwijderen
		if(this.getChildren().contains(mediaView)) {
			this.getChildren().remove(mediaView);
			//de kinderen terug toevoegen om weer te geven
			this.getChildren().addAll(childeren);
			this.heightProperty().removeListener(sizeListener);
			this.widthProperty().removeListener(sizeListener);
		}
		
		//afspelen stoppen indien bezig
		if(this.player!= null) {
			player.stop();
		}
		this.media = null;
		this.player = null;
	};
}
