/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javaFXpanels.TeamLogin;

import java.io.IOException;

import javaFXToSwing.FXMLToPanel;
import javafx.scene.layout.AnchorPane;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;


/**
 *
 * @author vrolijkx
 */
public class TeamLogintest {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                
                JFrame frame = new JFrame("JavaFX 2 in Swing");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                
                FXMLToPanel<AnchorPane, TeamLoginController> test = null;
				try {
					test = new FXMLToPanel<AnchorPane,TeamLoginController>(TeamLoginController.class.getResource("teamLogin.fxml"));
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
