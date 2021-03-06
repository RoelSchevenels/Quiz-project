package server.main;

import javaFXToSwing.PaneToPanel;

import javafx.scene.layout.AnchorPane;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import screenManger.ScreenManager;

public class testQuizMasterDisplay {

	/**
	 * Testklasse voor LoginPanel
	 * 
	 * @author De Meersman Vincent
	 */
	public static void main(String[] args) {
		
	        SwingUtilities.invokeLater(new Runnable() {
	            @Override
	            public void run() {
	                
	                JFrame frame =  ScreenManager.getInstance().getFrame("main");
	                frame.setTitle("Test");
	                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	                
	                PaneToPanel<AnchorPane> p = new PaneToPanel<AnchorPane>(new QuizMasterDisplay());
	                
	                frame.add(p);
	                frame.setSize(800, 500);
	                frame.setLocationRelativeTo(null);
	                frame.setVisible(true);

	            }
	        });
	}

}

