package javaFXpanels.MediaPane;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;

import javaFXToSwing.FXMLToPanel;
import javaFXToSwing.PaneToPanel;

import javafx.scene.layout.AnchorPane;
import javafx.scene.media.MediaException;

import javax.swing.JFrame;


import BuisinesLayer.resources.MediaResource;



public class  MediaControllerTest extends JFrame {
	private static final long serialVersionUID = 7658138729825914696L;
	final static String TEST_MOVIE = "~/Desktop/HaroldAndKumar.mp4";
	
	public MediaControllerTest() {
		super("Controller");
		FXMLToPanel<AnchorPane, MediaControllerController> controller = null;
		
		PaneToPanel<MediaPlayerPane> media = 
				new PaneToPanel<MediaPlayerPane>(new MediaPlayerPane());
		try {
			controller = new FXMLToPanel<AnchorPane,MediaControllerController>(MediaControllerController
					.class.getResource("mediaController.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		this.setLayout(new BorderLayout());
		
		this.add(media,BorderLayout.CENTER);
		this.add(controller,"South");
		
		MediaResource r = null;
		try {
			r = new MediaResource(new File(TEST_MOVIE.replace("~", System.getProperty("user.home"))));
		} catch (MediaException | IOException e) {
			e.printStackTrace();
		}
		
		
		media.getContentPane().SetMovie(r);
		media.getContentPane().setController((MediaPaneController) controller.getController());
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.setSize(800, 800);
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		new MediaControllerTest();
	}
	
	
}
