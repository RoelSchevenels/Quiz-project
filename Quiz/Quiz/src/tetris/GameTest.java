package tetris;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameTest {
	public static void main(String arg[])
	{
		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(400, 400));
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
		Game.initAndGet(panel);
	}
}
