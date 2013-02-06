/**
 * een wrapper voor MediaPlayerPane
 * @author vrolijkx
 */
package test;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javaFXpanels.MediaPane.MediaPlayerPane;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javax.swing.JPanel;

/**
 *
 * @author vrolijkx
 */
public class MediaPlayerPanel extends JPanel {
	private static final long serialVersionUID = 2353651882003896983L;
	private static JFXPanel fxContainer;
	private MediaPlayerPane contentPane;


	public MediaPlayerPanel() {
		super(new BorderLayout());
		fxContainer = new JFXPanel();
		fxContainer.setPreferredSize(new Dimension(1000, 1000));
		add(fxContainer, BorderLayout.CENTER);
		// create JavaFX scene
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				createScene();
			}
		});
	}

	private void createScene() {
		//het fxml bestand laden dat zicht bij de backupController bevindt
		contentPane = new MediaPlayerPane();
		Scene s = new Scene(contentPane);
		fxContainer.setScene(s);
		Dimension dPref = new Dimension((int)contentPane.getPrefWidth(), (int)contentPane.getPrefHeight());
		Dimension dMin = new Dimension((int)contentPane.getMinWidth(),(int)contentPane.getMaxWidth());
		fxContainer.setPreferredSize(dPref);
		fxContainer.setMinimumSize(dMin);

	}

	public MediaPlayerPane getContentPane() {
		return contentPane;
	}

}

