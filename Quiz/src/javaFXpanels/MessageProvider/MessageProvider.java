/**
 * een klasse om messages weer te geven zonder
 * vervelende popups.
 * @author vrolijkx
 */

package javaFXpanels.MessageProvider;


import java.util.ArrayList;

import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.animation.TranslateTransitionBuilder;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
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
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
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
	private Rectangle clipingBox;
	private SimpleObjectProperty<Pane> contentPane;
	private SimpleBooleanProperty expanded;


	public MessageProvider(AnchorPane parent) {
		expanded = new SimpleBooleanProperty(false);
		contentPane = new SimpleObjectProperty<Pane>();
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
				locatePane(contentPane.get());	
			}
		};

		parent.heightProperty().addListener(sizeChange);
		parent.widthProperty().addListener(sizeChange);

		//cliping geeft het gebied weer da mag weergegven worden
		clipingBox.widthProperty().bind(parent.widthProperty());
		clipingBox.heightProperty().bind(parent.heightProperty());
		messagePane.clipProperty().set(clipingBox);

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
				.interpolator(Interpolator.EASE_OUT)
				.toY(0.0)
				.build();

		
		//de hide transition
		hideTransition = TranslateTransitionBuilder
				.create()
				.duration(Duration.millis(500))
				.node(messagePane)
				.interpolator(Interpolator.EASE_IN)
				.build();

		//als de contentPane veranderd
		
		hideTransition.setOnFinished(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				contentPane.get().setVisible(false);
				expanded.set(false);
			}
		});
		
		contentPane.addListener(new ChangeListener<Pane>() {
			@Override
			public void changed(ObservableValue<? extends Pane> observable,
					Pane oldValue, Pane newValue) {
				if(newValue!= null) {
					showTransition.setNode(newValue);
					hideTransition.setNode(newValue);
					
					hideTransition.toYProperty().bind(newValue.heightProperty().add(20).multiply(-1));
					
					newValue.setTranslateY(-newValue.getPrefHeight() - 20);
					if(!parent.getChildren().contains(newValue)) {
						parent.getChildren().add(newValue);
					}
					//newValue.setClip(clipingBox);
					newValue.getStylesheets()
						.add(this.getClass().getResource("message.css").toExternalForm());
					newValue.getStyleClass().add("content-pane");
					locatePane(newValue);
				}
				
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
			System.out.println("Failed to load style sheet.");
		}
		locatePane(messagePane);

		parent.getChildren().add(messagePane);

		clipingBox = new Rectangle();
	}

	public void setStylesheet(String scheet) throws Exception{
		messagePane.getStylesheets().add(scheet);
	}

	private void locatePane(Pane p) {
		if(p!=null && parent != null) {
			double sideOfset = parent.getPrefWidth() / 5;

			AnchorPane.setTopAnchor(p,0.0);
			AnchorPane.setRightAnchor(p,sideOfset);
			AnchorPane.setLeftAnchor(p, sideOfset);
		}
	}

	public void hide() {
		if(expanded.get()) {
			

			for(Node n: notMouseTransparantChilderen) {
				n.setMouseTransparent(false);
			}
			notMouseTransparantChilderen.clear();
			hideTransition.play();
		}
	}

	private void setIcon(String name) {
		icon.setImage(new Image(this.getClass().getResource(name).toExternalForm()));
	}

	private void displayMessage(String message) {
		this.message.setText(message);
		calcPrefHeight();
		this.locatePane(messagePane);
		showPane(messagePane);
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
		messagePane.setTranslateY(-height - 20);
	}

	
	//de belangrijkste methode
	public void showPane(final Pane p) {
		if(expanded.get() && !contentPane.get().equals(p)) {
			expanded.addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observable,
						Boolean oldValue, Boolean newValue) {
					expanded.removeListener(this);
					showPane(p);	
				}

			});
			hide();
		} else if(!expanded.get()){
			contentPane.set(p);
			p.setVisible(true);
			expanded.set(true);
			notMouseTransparantChilderen.clear();
			for(Node n: parent.getChildren()) {
				if(n != contentPane.get() && !n.isMouseTransparent()) {
					n.setMouseTransparent(true);
					notMouseTransparantChilderen.add(n);
				}
			}
			showTransition.play();
			p.requestFocus();
		}
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

}
