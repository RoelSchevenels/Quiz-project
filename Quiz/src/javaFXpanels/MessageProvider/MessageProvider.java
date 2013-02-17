/**
 * een klasse om messages weer te geven zonder
 * vervelende popups.
 * @author vrolijkx
 */

package javaFXpanels.MessageProvider;


import java.util.ArrayList;

import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;

import javafx.animation.TranslateTransition;
import javafx.animation.TranslateTransitionBuilder;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class MessageProvider {
	private AnchorPane parent;
	private AnchorPane messagePane;
	private ImageView icon;
	private Button hideButton;
	private Label message;
	private TranslateTransition hideTransition;
	private TranslateTransition showTransition;
	private ArrayList<Node> notMouseTransparantChilderen;
	
	
	public MessageProvider(AnchorPane parent) {
		notMouseTransparantChilderen = new ArrayList<Node>();
		this.parent = parent;
		initPane();
		initBindingsAndAnimation();
	}
	
	/**
	 * bind de events en creer de hide en show animatie
	 */
	private void initBindingsAndAnimation() {
		//luister naar resize van de parrent
		ChangeListener<Number> sizeChange = new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				locatePane();		
			}
		};
		
		parent.heightProperty().addListener(sizeChange);
		parent.widthProperty().addListener(sizeChange);
		
		hideButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				hide();	
			}
		});
		
		//de showTransition
		showTransition = TranslateTransitionBuilder
				.create()
				.duration(Duration.millis(500))
				.node(messagePane)
				.build();
		
		showTransition.byYProperty().bind(messagePane.heightProperty().add(9));
		//de hide transition
		hideTransition = TranslateTransitionBuilder
				.create()
				.duration(Duration.millis(500))
				.node(messagePane)
				.build();
		
		hideTransition.byYProperty().bind(messagePane.heightProperty().add(9).multiply(-1));
		
		//messagPane verbergen na hide trasition
		hideTransition.onFinishedProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<? extends Object> observable,
					Object oldValue, Object newValue) {
				messagePane.setVisible(false);
				
			}
		});
	}
	

	/**
	 * maak de gui aan
	 */
	private void initPane() {
		messagePane = new AnchorPane();
		message = new Label();
		icon = new ImageView();
		hideButton = new Button("Verberg");
		
		
		AnchorPane.setLeftAnchor(icon, 15.0);
		AnchorPane.setTopAnchor(icon, 10.0);
		
		AnchorPane.setBottomAnchor(hideButton, 10.0);
		AnchorPane.setRightAnchor(hideButton, 10.0);
		
		AnchorPane.setLeftAnchor(message, 75.0);
		AnchorPane.setTopAnchor(message, 20.0);
		AnchorPane.setRightAnchor(message, 30.0);
		AnchorPane.setBottomAnchor(message, 40.0);
		
		messagePane.setVisible(false);
		messagePane.setMinHeight(90);
		messagePane.setPrefHeight(90);
		messagePane.getChildren().addAll(message,icon,hideButton);
		
		messagePane.getStyleClass().add("messagePane");
		message.getStyleClass().add("messageLabel");
		icon.getStyleClass().add("messageIcon");
		hideButton.getStyleClass().add("HideButton");
		
		try {
			setStylesheet(this.getClass().getResource("message.css").toExternalForm());
		} catch(Exception e) {
			System.out.println("failed to load style sheet");
		}
		locatePane();
		
		parent.getChildren().add(messagePane);
		
		
	}
	
	public void setStylesheet(String scheet) throws Exception{
		messagePane.getStylesheets().add(scheet);
	}
	

	private void locatePane() {
		double sideOfset = parent.getPrefWidth() / 5;
		double topOfset = (messagePane.getPrefHeight()*-1)-10;
		
		AnchorPane.setTopAnchor(messagePane,topOfset);
		AnchorPane.setRightAnchor(messagePane,sideOfset);
		AnchorPane.setLeftAnchor(messagePane, sideOfset);
	}
	
	
	private void hide() {
		//hideTransition.setByY(-messagePane.getPrefHeight()-9);
		hideTransition.play();
		
		for(Node n: notMouseTransparantChilderen) {
			n.setMouseTransparent(false);
		}
	}
	
	
	private void show() {
		messagePane.setVisible(true);
		//showTransition.setByY(messagePane.getPrefHeight()+9);
		showTransition.play();
		
		
		notMouseTransparantChilderen.clear();
		for(Node n: parent.getChildren()) {
			if(n != messagePane && !n.isMouseTransparent()) {
				n.setMouseTransparent(true);
				notMouseTransparantChilderen.add(n);
			}
		}
		
		
	}

	
	private void setIcon(String name) {
		icon.setImage(new Image(this.getClass().getResource(name).toExternalForm()));
	}
	
	
	private void displayMessage(String message) {
		this.message.setText(message);
		calcPrefHeight();
		this.locatePane();
		show();
	}
	
	
	public void showWarning(String message) {
		setIcon("warning.png");
		displayMessage(message);	
	};
	
	public void showError(String message) {
		setIcon("error.png");
		displayMessage(message);
	}
	
	/**
	 * 
	 * @param message One or more rules on a string
	 */
	public void showInfo(String message) {
		setIcon("information.png");
		displayMessage(message);
	}
	
	/**
	 * hoogte bepalen aan de hand van de font en het aantal regels
	 * van de message
	 */
	private void calcPrefHeight() {
		String text = message.getText();
		int rowCount = text.split("\n").length;
		FontMetrics m = Toolkit.getToolkit().getFontLoader().getFontMetrics(message.getFont());
		double height = (m.getLineHeight() * rowCount) + 60;
		
		height = height > 90? height : 90.0;
		
		messagePane.setPrefHeight(height);	
	}

	
}
