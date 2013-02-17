/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javaFXToSwing;

import java.io.IOException;
import javaFXpanels.Backup.BackupController;
import javafx.scene.layout.AnchorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


/**
 *
 * @author vrolijkx
 */
public class BackupPaneltest {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                
                JFrame frame = new JFrame("JavaFX 2 in Swing");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                
                FXMLToPanel<AnchorPane, BackupController> test = null;
				try {
					test = new FXMLToPanel<AnchorPane,BackupController>(BackupController.class.getResource("FXMLbackup.fxml"));
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
