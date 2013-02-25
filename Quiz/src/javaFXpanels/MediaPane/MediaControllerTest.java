package javaFXpanels.MediaPane;

import java.io.File;
import java.io.IOException;

import javaFXToSwing.FXMLToPanel;

import javafx.scene.layout.AnchorPane;
import javax.swing.JFrame;
import BussinesLayer.resources.MediaResource;
import GUI.quizMaster.MediaFrame;



public class  MediaControllerTest extends JFrame {
	private static final long serialVersionUID = 7658138729825914696L;
	final static String TEST_MOVIE = "~/Desktop/HaroldAndKumar.mp4";
	
	
	public static void main(String[] args) {
		try {
			MediaFrame f = new MediaFrame();
			f.setVisible(true);
			
			
			FXMLToPanel<AnchorPane, MediaControllerController> controller = new FXMLToPanel<AnchorPane,MediaControllerController>(MediaControllerController
					.class.getResource("mediaController.fxml"));
			MediaResource r = new MediaResource(new File(TEST_MOVIE.replace("~", System.getProperty("user.home"))));
			
			
			f.setMovie(r);
			f.setController(controller.getController());
			System.out.println("frames maken");
			
			
			MediaFrame f2 = new MediaFrame();
			f2.setLocation(50, 50);
			f2.setVisible(true);
			f2.setMovie(r);
			f2.setController(controller.getController());
			f2.setDefaultCloseOperation(EXIT_ON_CLOSE);
			f2.setSize(500,500);
		
			JFrame contrFrame = new JFrame();
			contrFrame.add(controller);
			contrFrame.setVisible(true);
			contrFrame.pack();
			contrFrame.setSize(500,500);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	
}
