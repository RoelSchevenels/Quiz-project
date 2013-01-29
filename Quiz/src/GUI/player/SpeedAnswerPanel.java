package GUI.player;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;


public class SpeedAnswerPanel extends AnswerPanel
{
	private JButton btnPress;
	
	public SpeedAnswerPanel()
	{
		btnPress = new JButton("Druk af");
		add(btnPress);
		btnPress.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				//Send code to server. Whoever presses first gets to answer.
			}
		});
	}
}
