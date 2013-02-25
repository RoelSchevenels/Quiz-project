package screenManger;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JFrame;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;



//TODO: de ScreenWrappers locaal zetten;
public class ScreenWrapper {
	private static ArrayList<ScreenWrapper> screensList 
		= new ArrayList<ScreenWrapper>();
	private GraphicsDevice screen;
	private String id;
	private ObservableList<FrameWrapper> frames;
	
	
	
	private ScreenWrapper(GraphicsDevice g) {
		this.setScreen(g);
		this.id = g.getIDstring();
		frames = FXCollections.observableArrayList();
		screensList = new ArrayList<ScreenWrapper>();
	}

	public GraphicsDevice getScreen() {
		return screen;
	}

	public void setScreen(GraphicsDevice screen) {
		this.screen = screen;
	}
	
	public void remove(FrameWrapper frameWrapper) {
		if(frames.contains(frameWrapper)) {
			frames.remove(frameWrapper);
		}	
	};
	
	public void add(FrameWrapper frameWrapper) {
		if(!frames.contains(frameWrapper)) {
			frames.add(frameWrapper);
		}
	}
	
	public int getFrameCount() {
		return frames.size();
	}
	
	public ObservableList<FrameWrapper> getFrames() {
		return frames;
	}
	
	/**
	 * komt dit screendevice overeen met een al bestaand scherm
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ScreenWrapper) {
			ScreenWrapper w = (ScreenWrapper) obj;
			if(w.id.equals(this.id)) {
				return true;
			}
		}
		
		return false;
	}
	
	public GraphicsDevice getGraphicsDevice() {
		return screen;
	}

	/**
	 * @return het vierkant dat het scherm voorsteld
	 */
	public Rectangle getBounds() {
		return screen.getDefaultConfiguration().getBounds();
	}

	public String getId() {
		return id;
	}
	
	private static void updateScreensList() {
		GraphicsDevice[] schemDevices = GraphicsEnvironment
				.getLocalGraphicsEnvironment()
				.getScreenDevices();
		ArrayList<ScreenWrapper> tempScreensList = new ArrayList<ScreenWrapper>();
		
		schermlus:for(GraphicsDevice g: schemDevices) {
			//zit het scherm al in de lus?
			for(ScreenWrapper w : screensList) {
				if(w.id.equals(g.getIDstring())) {
					tempScreensList.add(w);
					continue schermlus;
				}
			}
			
			//maak een nieuwe schermwrapper
			tempScreensList.add(new ScreenWrapper(g));			
		}
		
		screensList = tempScreensList;
	}
	
	public static ArrayList<ScreenWrapper> getScreens() {
		//updaten de schermen moesten maar eens veranderd zijn
		updateScreensList();
		return screensList;
		
	}
	
	public static ScreenWrapper getScreen(String id) {
		updateScreensList();
		for(ScreenWrapper w: screensList) {
			if(w.getId().equals(id)) {
				return w;
			} else if(id.equals("primary") && w.getScreen()
						.equals(GraphicsEnvironment
								.getLocalGraphicsEnvironment()
								.getDefaultScreenDevice())) {
				
			}
		}
		
		//het scherm is wss niet aangekopppeld op dit moment
		return null;
	}
	
	/**
	 * @param jf
	 * @return de schermen waarop het frame zich op bevindt
	 */
 	public static ArrayList<ScreenWrapper> getScreens(JFrame jf) {	
		ArrayList<ScreenWrapper> list = new ArrayList<ScreenWrapper>();
		
		Rectangle bounds = jf.getBounds() != null? jf.getBounds() : new Rectangle(-5,-5,0,0);
		for(ScreenWrapper w: getScreens()) {
			if(intersect(bounds, w.getBounds())) {
				list.add(w);
			}
		}
		return list;
	}

	/**
	 * Kijkt over 2 vierkanten overlappen 
	 * 
	 * @param r1
	 * @param r2
	 * @return true als de vierkanten overlappen false anders
	 */
	public static boolean intersect(Rectangle r1, Rectangle r2) {
		int x1 = r1.x + r1.width / 2;
		int x2 = r2.x + r2.width / 2;
		int y1 = r1.y + r1.height / 2;
		int y2 = r2.y + r2.height /2;
		
		
		return Math.abs(x1 - x2) < (Math.abs(r1.width + r2.width) / 2) 
				&& (Math.abs(y1 - y2) < (Math.abs(r1.height + r2.height) / 2));
		
	}
}
