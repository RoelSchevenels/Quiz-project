package screenManger;

import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.swing.JFrame;


//TODO: proberen rekening te houden met de visibility van de frames
public class FrameWrapper {
	private boolean prefersFullScreen;
	private String name;
	private JFrame frame;
	private String prefScreenId;
	private ObservableList<ScreenWrapper> screens;
	
	public FrameWrapper(JFrame f,String name, ScreenWrapper prefScreen) {
		this(f,name);
		setPrefScreen(prefScreen);
		
	}
	
	public FrameWrapper(JFrame f,String name) {
		this.name = name;
		this.frame = f;
		prefersFullScreen = false;
		screens = FXCollections.observableArrayList();
		//de schermlijst laden
		getScreens();
		f.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent e) {
				getScreens();
			}
			
			@Override
			public void componentResized(ComponentEvent e) {
				getScreens();
			}
			
		});	
	}
		
	public void setPrefScreen(ScreenWrapper w) {
		prefScreenId = w.getId();
		if(w.getFrameCount() == 0) {
			this.relocate(w);
		}
	}
	
	public ScreenWrapper getPrefScreen() {
		if(prefScreenId==null) {
			return null;
		} else {
			return ScreenWrapper.getScreen(prefScreenId);
		}
	}
	
	public void setFullScreen(ScreenWrapper w) {
		if(w!= null) {
			w.getScreen().setFullScreenWindow(frame);
		}
	}
	

	
	/**
	 * verplaats het frame van zijn huidige scherm naar een nieuw scherm
	 * @param newScreen
	 */
	public void relocate(ScreenWrapper newScreen) {
		Rectangle s = newScreen.getBounds();
		Rectangle f;
		if(frame.isVisible()) {
			f = frame.getBounds();
		} else {
			f = new Rectangle(10, 10,frame.getWidth(),frame.getHeight());
		}
		
		
		//centreren op het nieuw scherm
		int width = (int) Math.min(s.getWidth(), f.getWidth());
		int height = (int) Math.min(s.getHeight(), f.getWidth());
		int x = (int) ((width - f.getWidth()) / 2);
		int y = (int) ((height - f.getHeight()) / 2);
		frame.setSize(width, height);
		frame.setLocation(x, y);
	}
	
	public void dispose() {
		//zorgen dat screenWrappers niet meer refereven naar deze frameWrapper
		for(ScreenWrapper w: screens) {
			remove(w);
		}
	}
	
	//screen wrapper zal dit aanroepen
	public void remove(ScreenWrapper screenWrapper) {
		if(this.screens.contains(screenWrapper)) {
			screens.remove(screenWrapper);
			screenWrapper.remove(this);
		}
	}
	
	private void getScreens() {
		ArrayList<ScreenWrapper> newscreens = new ArrayList<ScreenWrapper>();
		
		//vorige schermen verwijderen
		for(ScreenWrapper sw: screens) {
			if(!newscreens.contains(sw)) {
				sw.remove(this);
			}
		}
		
		for(ScreenWrapper sw: newscreens) {
			sw.add(this);
		}
		
		screens.setAll(newscreens);
	}

	public JFrame getFrame() {
		return frame;
	}
	
	public String getName() {
		return name;
	}

	public boolean isPrefersFullScreen() {
		return prefersFullScreen;
	}

	public void setPrefersFullScreen(boolean prefersFullScreen) {
		this.prefersFullScreen = prefersFullScreen;	
	}
}