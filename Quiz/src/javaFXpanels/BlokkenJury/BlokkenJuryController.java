package javaFXpanels.BlokkenJury;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javaFXToSwing.FXMLToPanel;
import javaFXpanels.MessageProvider.MessageProvider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import Protocol.submits.TetrisStartSubmit;

public class BlokkenJuryController implements Initializable {
	private MessageProvider messageProvider;
	@FXML
	private ComboBox<Integer> cmb;
	@FXML
	private AnchorPane pane;
	
	@FXML
	private void displayTetris()
	{
		// TODO tetris weergeven
	}
	
	@FXML 
	private void startPlayerOne() {	tetrisStart(1);	}
	@FXML
	private void startPlayerTwo() { tetrisStart(2); }
	
	private void tetrisStart(int player)
	{
		int pieceCount = (int) cmb.getValue();
		System.out.println(pieceCount);
		try {
			new TetrisStartSubmit("player"+player,pieceCount).send();
			throw new IOException();
		} catch (IOException e) {
			messageProvider.showError("Geen verbinding \nmet server");
		}
	}

	@Override
	public void initialize(URL url, ResourceBundle rb)
	{
		messageProvider = new MessageProvider(pane);
		ObservableList<Integer> blocks = FXCollections.observableArrayList(1,2,3,4,5);
		cmb.setItems(blocks);
		cmb.setValue(1);
	}
	
	public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                
                JFrame frame = new JFrame("JavaFX 2 in Swing");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                
                FXMLToPanel<AnchorPane, BlokkenJuryController> test = null;
				try {
					test = new FXMLToPanel<AnchorPane,BlokkenJuryController>(BlokkenJuryController.class.getResource("BlokkenJury.fxml"));
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
