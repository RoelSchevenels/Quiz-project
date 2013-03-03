package javaFXpanels.LoginServer;

import java.io.IOException;

import javaFXToSwing.FXMLToPanel;
import javafx.scene.layout.AnchorPane;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class TestLoginServer {

	/**
	 * Testklasse voor LoginPanel
	 * 
	 * @author De Meersman Vincent
	 */
	public static void main(String[] args) {
		
	        SwingUtilities.invokeLater(new Runnable() {
	            @Override
	            public void run() {
	                
	                JFrame frame = new JFrame("JavaFX 2 in Swing");
	                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	                
	                FXMLToPanel<AnchorPane, LoginPanelServer> test = null;
					try {
						test = new FXMLToPanel<AnchorPane,LoginPanelServer>(LoginPanelServer.class.getResource("LoginServer.fxml"));
					} catch (IOException e) {
						e.printStackTrace();
					}
	                
	                frame.setContentPane(test);
	                frame.pack();
	                frame.setLocationRelativeTo(null);
	                frame.setVisible(true);

	            }
	        });
	}

}
