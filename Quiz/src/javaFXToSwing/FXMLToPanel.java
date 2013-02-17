
package javaFXToSwing;


import java.awt.Dimension;
import java.io.IOException;
import java.net.URL;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;



public class FXMLToPanel<T extends Pane,C extends Initializable> extends JFXPanel {
	private static final long serialVersionUID = 4273971103636857634L;
	private T contentPane;
	private C controller;



	public  FXMLToPanel(URL FXMLdocument) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(FXMLdocument);
		contentPane = (T) loader.load();
		controller = loader.getController();
		init();
	}
	
	private void init() {
		setPreferredSize(new Dimension(1000, 1000));
		// create JavaFX scene
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				createScene();
			}
		});
	}
	
	private void createScene() {
		Scene s = new Scene(contentPane);
		setScene(s);
		Dimension dPref = new Dimension((int)contentPane.getPrefWidth(), (int)contentPane.getPrefHeight());
		Dimension dMin = new Dimension((int)contentPane.getMinWidth(),(int)contentPane.getMaxWidth());
		setPreferredSize(dPref);
		setMinimumSize(dMin);
	}	

	public T getContentPane() {
		return contentPane;
	}

	public  C getController() {
		return controller;
	}
}

