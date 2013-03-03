package GUI.quizMasterBlokken;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javaFXToSwing.PaneToPanel;
import javaFXpanels.MediaPane.MediaPaneController;
import javaFXpanels.MediaPane.MediaPlayerPane;

import javax.swing.JFrame;
import javax.swing.JPanel;

import BussinesLayer.resources.MediaResource;
/**
 * @author Roel
 */
public class MediaFrame extends JFrame {
	private static final long serialVersionUID = 7658138729825914696L;
	private PaneToPanel<MediaPlayerPane> media;
	private JPanel displayPanel;
	
	public MediaFrame() {
		super("media");
		this.setLayout(new BorderLayout());
		displayPanel = new JPanel();
		displayPanel.setLayout(new BorderLayout());
		this.add(displayPanel,BorderLayout.CENTER);
		
		media = new PaneToPanel<MediaPlayerPane>(new MediaPlayerPane());
		
		displayPanel.add(media,BorderLayout.CENTER);
		
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setPreferredSize(new Dimension(900,400));
	}
	
	public MediaPlayerPane getPlayer() {
		return media.getContentPane();
	}
	
	public void setController(final MediaPaneController c) {
		media.getContentPane().setController(c);
		
	}
	
	public void setMovie(final MediaResource r) {
		media.getContentPane().setMovie(r);

	}
	
	public void setMusic(final MediaResource r) {
		media.getContentPane().SetAudio(r);
	}
	
	public JPanel getDisplayPanel(boolean clear)
	{
		if(clear)
			displayPanel.removeAll();
		return displayPanel;
	}

}
