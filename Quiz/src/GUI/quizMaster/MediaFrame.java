package GUI.quizMaster;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javaFXToSwing.PaneToPanel;
import javaFXpanels.MediaPane.MediaControllerController;
import javaFXpanels.MediaPane.MediaPaneController;
import javaFXpanels.MediaPane.MediaPlayerPane;

import javax.swing.JFrame;

import BussinesLayer.resources.MediaResource;

public class MediaFrame extends JFrame {
	private static final long serialVersionUID = 7658138729825914696L;
	private PaneToPanel<MediaPlayerPane> media;
	
	public MediaFrame() {
		super("media");
		this.setLayout(new BorderLayout());
		
		media = new PaneToPanel<MediaPlayerPane>(new MediaPlayerPane());

		this.setLayout(new BorderLayout());
		this.add(media,BorderLayout.CENTER);
		
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setPreferredSize(new Dimension(900,400));
	}
	
	public MediaPlayerPane getPlayer() {
		return media.getContentPane();
	}
	
	public void setController(MediaPaneController c) {
		media.getContentPane().setController(c);
	}
	
	
	public void setMovie(MediaResource r) {
		media.getContentPane().setMovie(r);
	}
	
	public void setMusic(MediaResource r) {
		media.getContentPane().SetAudio(r);
	}

}
