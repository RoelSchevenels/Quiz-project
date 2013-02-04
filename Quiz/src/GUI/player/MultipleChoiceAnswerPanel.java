/**
 * @author Jorne De Smedt
 */
package GUI.player;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JButton;

public class MultipleChoiceAnswerPanel extends AnswerPanel
{
	private ArrayList<JButton> answerButtons;
	
	public static final int SINGLE_COLUMN = 0;
	public static final int DOUBLE_COLUMN = 1;
	public static final int TRIPPLE_COLUMN = 2;
	public static final int QUADRUPLE_COLUMN = 3;
	public static final int SINGLE_ROW = 4;
	public static final int DOUBLE_ROW = 5;
	public static final int TRIPPLE_ROW = 6;
	public static final int QUADRUPLE_ROW = 7;
	public static final int SQUARE = 8;
	
	
	public MultipleChoiceAnswerPanel()
	{
		answerButtons = new ArrayList<JButton>();
	}
	
	public MultipleChoiceAnswerPanel(ArrayList<String> answers)
	{
		answerButtons = new ArrayList<JButton>();
		for (String answer : answers)
		{
			answerButtons.add(new JButton(answer));
		}
	}
	
	/**Draw the buttons
	 *
	 *
	 * @param organization The way it gets organized
	 */
	public void drawButtons(int organization)
	{
		switch (organization)
		{
		case SINGLE_COLUMN:
			setLayout(new GridLayout(answerButtons.size(), 1, 5, 5));
			break;
		case DOUBLE_COLUMN:
			setLayout(new GridLayout(Math.round(answerButtons.size()/2.0f), 2, 5, 5));
			break;
		case TRIPPLE_COLUMN:
			setLayout(new GridLayout(Math.round(answerButtons.size()/3.0f), 3, 5, 5));
			break;
		case SINGLE_ROW:
			setLayout(new GridLayout( 1, answerButtons.size(), 5, 5));
			break;
		case DOUBLE_ROW:
			setLayout(new GridLayout( 2, Math.round(answerButtons.size()/2.0f), 5, 5));
			break;
		case TRIPPLE_ROW:
			setLayout(new GridLayout( 3, Math.round(answerButtons.size()/3.0f), 5, 5));
			break;
		default:
			break;
		}
		
		for (JButton button : answerButtons)
		{
			add(button);
		}
	}
}
