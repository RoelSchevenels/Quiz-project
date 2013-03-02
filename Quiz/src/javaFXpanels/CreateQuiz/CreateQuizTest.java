package javaFXpanels.CreateQuiz;

import java.io.IOException;

import javaFXToSwing.FXMLToPanel;
import javaFXpanels.Backup.BackupController;
import javafx.scene.layout.AnchorPane;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import Util.ConnectionUtil;

public class CreateQuizTest {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ConnectionUtil.StartDataBase();
                
                JFrame frame = new JFrame("JavaFX 2 in Swing");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                
                FXMLToPanel<AnchorPane, QuizMakerController> test = null;
				try {
					test = new FXMLToPanel<AnchorPane,QuizMakerController>(QuizMakerController.class.getResource("quizMaker.fxml"));
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
