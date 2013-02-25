package tetris;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import network.ConnectionWorker;
import Protocol.AuthSubmit;
import Protocol.TetrisStartSubmit;

public class JuryClient extends ConnectionWorker{
	private JFrame frame;
	private JPanel panel;
	private JSpinner pieces;
	
	public JuryClient() throws UnknownHostException, IOException
	{
		super(new Socket("127.0.0.1", 1337), 0);
		makeGUI();
		send(new AuthSubmit("jury"));
	}
	
	public static void main(String arg[])
	{
		try {
			new JuryClient().run();
		} catch (UnknownHostException e) {
			System.out.println("Could not find server.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void makeGUI()
	{
		frame = new JFrame("Jury client");
		panel = new JPanel();
		pieces = new JSpinner(new SpinnerNumberModel(1,1,5,1));
		frame.add(panel);
		frame.add(pieces, BorderLayout.NORTH);
		
		JButton one = new JButton("player1");
		JButton two = new JButton("player2");
		ActionHandler ah = new ActionHandler();
		one.addActionListener(ah);
		two.addActionListener(ah);
		panel.add(one);
		panel.add(two);
		
		frame.setSize(300,200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	@Override
	public void handleData(Object data)
	{

	}


	@Override
	public void handleDeath(int id)
	{
		System.out.println("Server closed the connection.");
	}
	
	private class ActionHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent ae)
		{
			int pieceCount = (Integer)pieces.getValue();
			String text =((JButton)ae.getSource()).getText();
			send(new TetrisStartSubmit(text,pieceCount));
		}
	}
}
