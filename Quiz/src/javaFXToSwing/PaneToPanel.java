
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



public class PaneToPanel<T extends Pane> extends JFXPanel {
	private static final long serialVersionUID = 4273971103636857634L;
	private T contentPane;
	private Initializable controller;

	public PaneToPanel(T contentPane) {
		this.contentPane = contentPane;
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

}

