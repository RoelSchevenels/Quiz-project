package GUI.player;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

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
		
		btnChange = new JButton("Change Question");
		btnChange.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				pnlPlayer.ChangeQuestion("This is the new question?", 0, false);
			}
		});
		add(btnChange, BorderLayout.NORTH);
		
		pnlPlayer = new PlayerPanel();
		add(pnlPlayer, BorderLayout.CENTER);
		
		
		
		setVisible(true);
	}
	public static void main(String[] args)
	{
		new TestPlayerGui();
	}

}
