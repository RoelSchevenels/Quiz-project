/**
 * @author Jorne De Smetd
 */
package GUI.player;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import BussinesLayer.QuizMaster;
import BussinesLayer.questions.*;

public class TestPlayerGui extends JFrame
{

	/**
	 * @param args
	 */
	
	private PlayerPanel pnlPlayer;
	private JButton btnChange, btnFoto, btnVideo, btnMusic, btnMultipleChoice;
	
	public TestPlayerGui()
	{
		setSize(800, 600);
		setTitle("Player Test");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		JPanel pnlButtons = new JPanel(new FlowLayout());
		btnChange = new JButton("Standard Question");
		btnChange.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				QuizMaster qm = new QuizMaster("Bob", "bla");
				Question question = new StandardQuestion(qm, "What does the scouter say about his power-level?", "IT'S OVER 9000!!!");
				pnlPlayer.setQuestion(question);
				pnlPlayer.changeQuestion(false);
				//pnlPlayer.ChangeQuestion("This is the new question?", 0, false);
			}
		});
		pnlButtons.add(btnChange);
		
		btnMultipleChoice = new JButton("Multiple Choice");
		btnMultipleChoice.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				QuizMaster qm = new QuizMaster("Bob", "bla");
				MultipleChoise question = new MultipleChoise(qm);
				question.setQuestion("How many Namekians does it take to screw in a light bulb?");
				question.addValue("Just one");
				question.addValue("Two");
				question.addValue("Three");
				question.addValue("Their whole race");
				pnlPlayer.setQuestion(question);
				pnlPlayer.changeQuestion(false);
				//pnlPlayer.ChangeQuestion("This is the new question?", 0, false);
			}
		});
		pnlButtons.add(btnMultipleChoice);
		
		
		add(pnlButtons, BorderLayout.NORTH);
		pnlPlayer = new PlayerPanel();
		add(pnlPlayer, BorderLayout.CENTER);
		
		
		
		setVisible(true);
	}
	public static void main(String[] args)
	{
		new TestPlayerGui();
	}

}
