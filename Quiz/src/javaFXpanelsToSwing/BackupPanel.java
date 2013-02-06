/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javaFXpanelsToSwing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javaFXpanels.Backup.FXMLBackupController;
import javaFXpanels.Backup.backupPane;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * @author vrolijkx
 */
public class BackupPanel extends JPanel {
    private static JFXPanel fxContainer;

    
    public BackupPanel() {
    	super(new BorderLayout());
        fxContainer = new JFXPanel();
        fxContainer.setPreferredSize(new Dimension(1000, 1000));
        add(fxContainer, BorderLayout.CENTER);
        // create JavaFX scene
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                createScene();
            }
        });
    }
    
    private void createScene() {
        try {
        	//het fxml bestand laden dat zicht bij de backupController bevindt
            AnchorPane p = backupPane.getBackupPane();
            Scene s = new Scene(p);
            fxContainer.setScene(s);
            Dimension dPref = new Dimension((int)p.getPrefWidth(), (int)p.getPrefHeight());
            Dimension dMin = new Dimension((int)p.getMinWidth(),(int)p.getMaxWidth());
            fxContainer.setPreferredSize(dPref);
            fxContainer.setMinimumSize(dMin);
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }   
    }

    
    //TODO: main verwijderen
    /**
     * test methode
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
                } catch (Exception e) {
                }
                
                JFrame frame = new JFrame("JavaFX 2 in Swing");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                
                JPanel test = new BackupPanel();
                
                frame.setContentPane(test);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

            }
        });
    }

}
