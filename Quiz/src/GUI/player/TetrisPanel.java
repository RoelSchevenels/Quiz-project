package GUI.player;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Protocol.submits.TetrisSubmit;
/**
 * @author Roel
 */
public class TetrisPanel extends JPanel {
	private static final long serialVersionUID = 5882612241445813868L;
	
	private int playernum;
	private JLabel player;
	private JLabel instructions;
	
	public TetrisPanel(int playerNumber)
	{
		this.playernum = playerNumber;
		build();
	}
	
	public TetrisPanel()
	{
		build();
	}
	
	private void build()
	{
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(200,200));
		setFocusable(true);
		player = new JLabel("<html><br />Player " + ((playernum == 0) ? "" : playernum)+"</html>");
		instructions = new JLabel("<html><center>←↓→ om te bewegen,<br />↑ om te draaien.</center></html>");
		player.setHorizontalAlignment(SwingConstants.CENTER);
		instructions.setHorizontalAlignment(SwingConstants.CENTER);
		add(player, BorderLayout.NORTH);
		add(instructions);
		addKeyListener(new KeyHandler());
		requestFocus();
	}
	
	public static void main(String arg[])
	{
		JFrame f = new JFrame("Tetris");
		f.add(new TetrisPanel(1));
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.pack();
		f.setVisible(true);
	}
	
	private class KeyHandler extends KeyAdapter{

		@Override
		public void keyPressed(KeyEvent ke)
		{
			int keycode = ke.getKeyCode();
			if(keycode >= 37 && keycode <= 40){
				char[] keys = {'l', 'u', 'r', 'd'};	// Left, up (rotate), right, down
				char dir = keys[keycode-37];
				
				try {
					new TetrisSubmit(dir).send();
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "Kan geen verbinding maken met de server");
					// TODO : MessageProvider hiervoor gebruiken? Kan dat vanuit een JPanel ?
				}
			}
		}	
	}
}
