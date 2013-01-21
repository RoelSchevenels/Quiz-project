package GUI.player;

import java.awt.BorderLayout;
import java.awt.TextArea;

import javax.swing.JButton;
import javax.swing.JTextArea;

public class OpenAnswerPanel extends AnswerPanel
{
	private JTextArea txtAnswer;
	private JButton btnSend;
	
	public OpenAnswerPanel()
	{
		setLayout(new BorderLayout());
		
		txtAnswer = new JTextArea();
		add(txtAnswer, BorderLayout.CENTER);
		
		btnSend = new JButton("Send");
		add(btnSend, BorderLayout.SOUTH);
	}
}
