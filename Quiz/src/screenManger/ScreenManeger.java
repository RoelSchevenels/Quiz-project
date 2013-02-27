/**
 * Een screen manager die bijhoud hoeveel schermen er zijn
 * en waar alle frame's zich op bevinden
 * @author vrolijkx
 */
package screenManger;


import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import javaFXToSwing.FXMLToPanel;
import javaFXpanels.MediaPane.MediaControllerController;
import javaFXpanels.MediaPane.MediaPaneController;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;


import BussinesLayer.resources.MediaResource;
import GUI.quizMaster.MediaFrame;


public class ScreenManeger {
	private static ScreenManeger instance;

	private HashSet<ScreenWrapper> availableScreens;
	private HashSet<FrameWrapper> visibleFrames;
	private HashMap<String, FrameWrapper> frames;
	


	private ScreenManeger() {
		frames = new HashMap<String, FrameWrapper>();
		availableScreens = new HashSet<ScreenWrapper>();
		visibleFrames = new HashSet<FrameWrapper>();
		updateScreens();
	}

	public JFrame getFrame(String name) {
		return getFrame(name,false);
	}

	public JFrame getFrame(String name, boolean prefFullScreen) {
		FrameWrapper w;
		if(frames.containsKey(name)) {
			w = frames.get(name);
		}else if (name.equals("media")) {
			throw new IllegalArgumentException("you can't use \"media\" as name");
		} else {
			JFrame f = new JFrame();
			w = new FrameWrapper(f,name);
			frames.put(name, w);
			bindFrame(f, w);
		}
		
		w.setPrefersFullScreen(prefFullScreen);
		
		if(prefFullScreen) {
			setFullScreenIfPossible(w);
		} else if(w.isFullScreen()) {
			removeFullscreen(w);
		}

		return w.getFrame();
	}

	public MediaFrame getMediaFrame() {
		MediaFrame f;
		if(frames.containsKey("media")) {
			f = (MediaFrame) frames.get("media").getFrame();
			f.setVisible(true);
		} else {
			f = new MediaFrame();
			FrameWrapper w = new FrameWrapper(f,"media");
			w.setPrefersFullScreen(true);
			frames.put("media", w);
			f.setVisible(true);
			setFullScreenIfPossible(w);
		}
		
		return f;
	}
	
	/**
	 * deze maakt een mediaframe aan op de event dispatch thread
	 * @param controller
	 * @param r
	 * @param movie
	 */
	public void createMediaFrame(final MediaPaneController controller,final MediaResource r, final boolean movie) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() { 
				MediaFrame f = getMediaFrame();
				f.setController(controller);
				if(movie) {
					f.setMovie(r);
				} else {
					f.setMusic(r);
				}
				
			}
		});		
	}
	
	public void clearMediaFrame() {
		if(frames.containsKey("media")) {
			getMediaFrame().getPlayer().clearPlayer();
		}
	}

	private void bindFrame(final JFrame f, final FrameWrapper w) {
		f.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentHidden(ComponentEvent e) {
				visibleFrames.remove(w);
			}

			@Override
			public void componentShown(ComponentEvent e) {
				visibleFrames.add(w);
			}
		});

		f.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				frames.remove(f.getName());
				f.dispose();

			}
		});

		//druk F5 om te kijken of er nieuwe schermen zijn
		f.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_F5) {
					updateScreens();
				}
			}
		});
	}

	public ScreenWrapper getEmptyScreen() {
		updateScreens();
		for(ScreenWrapper w: availableScreens) {
			if(w.getFrameCount() == 0 && w.getGraphicsDevice().getFullScreenWindow() == null) {
				return w;
			}
		}
		return null;
	}

	public int getAmountOfScreens() {
		updateScreens();
		return availableScreens.size();
	}

	public void updateScreens() {
		this.availableScreens = new HashSet<ScreenWrapper>(ScreenWrapper.getScreens());
	}


	public void removeFullscreen(FrameWrapper w) {
		try {
			w.getScreenList().get(0).getFrames().clear();
			w.getScreenList().get(0).getScreen().setFullScreenWindow(null);
		} catch(Exception e) {
			//een exception die normaal zich niet zou mogen voordoen
		}
	}
	
	private void setFullScreenIfPossible(final FrameWrapper w) {
		final ScreenWrapper emptyScreen = getEmptyScreen();

		//is er een leeg scherm vrij
		if(emptyScreen != null && emptyScreen.getGraphicsDevice().isFullScreenSupported()) {
			w.relocate(emptyScreen);
			w.setFullScreen(emptyScreen);
		};
		
		emptyScreen.getFrames().addListener(new InvalidationListener() {
			
			@Override
			public void invalidated(Observable arg0) {
				if(emptyScreen.getFrameCount() > 1) {
					try {
						emptyScreen.getGraphicsDevice().setFullScreenWindow(null);
					} catch(Exception e) {
						//gooit door een of andere reden een exception die niet echt nodig is
					}
				} else if (emptyScreen.getFrameCount() == 1 
						&& emptyScreen.getFrames().get(0).equals(w)
						&& emptyScreen.getGraphicsDevice().getFullScreenWindow() == null) {
					//w.setFullScreen(emptyScreen);
				}	
			}
		});
	}
	
	public static ScreenManeger getInstance() {
		if(instance == null) {
			instance = new ScreenManeger();
		}
		return instance;
	}


	//  veroorzaakt onverklaarbare applicatie crach zonder exception
	//	private void listenforScreens() {
	//		//dit is nodig om de javaFX thread aan te maken
	//		new JFXPanel();
	//		ObservableList<Screen> schermen = Screen.getScreens();
	//		schermen.addListener(new InvalidationListener() {
	//			@Override
	//			public void invalidated(Observable arg) {
	//				if(Screen.getScreens() != null && Screen.getScreens().size()!= 0) {
	//					System.out.println("screens changed");
	//				}
	//			}
	//		});
	//
	//	}

}
