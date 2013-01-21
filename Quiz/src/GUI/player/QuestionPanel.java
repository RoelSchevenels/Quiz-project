package GUI.player;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class QuestionPanel extends JPanel
{
	private JLabel lblQuestion;
	
	public QuestionPanel(String question)
	{
		lblQuestion = new JLabel(question);
		add(lblQuestion);
	}
	
	public String getQuestion()
	{
		return lblQuestion.getText();
	}
	
	public void setQuestion(String value)
	{
		lblQuestion.setText(value);
	}	
}
