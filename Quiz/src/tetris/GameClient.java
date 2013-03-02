package tetris;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import network.ConnectionWorker;
import Protocol.submits.AuthSubmit;
import Protocol.submits.IdRangeSubmit;
import Protocol.submits.TetrisSubmit;

public class GameClient extends ConnectionWorker{
	private static GameClient instance;
	
	private int baseId;
	private int maxIdCount;
	private int currentId;
	
	private GameClient() throws UnknownHostException, IOException
	{
		super(new Socket("127.0.0.1", 1337), 0);
		makeGUI();
		send(new AuthSubmit("player"));
	}
	
	public static GameClient getInstance()
	{
		if(instance == null){
			try {
				instance = new GameClient();
			} catch (UnknownHostException e) {
				System.out.println("Server niet gevonden");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return instance;
	}
	
	public static void main(String arg[])
	{
		getInstance().run();
	}
	
	public void makeGUI()
	{
		JFrame frame = new JFrame("Tetris client");
		JPanel panel = new JPanel();
		frame.add(panel);
		panel.add(new JLabel("Arrow keys for movement, up for rotation."));
		frame.addKeyListener(new KeyHandler());
		frame.setSize(300,200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	@Override
	public void handleData(Object data)
	{
		if(data instanceof IdRangeSubmit){
			IdRangeSubmit irs = (IdRangeSubmit)data;
			
			baseId = irs.getMin();
			maxIdCount = irs.getMax() - baseId;
			currentId = 0;
		}
	}


	@Override
	public void handleDeath(int id)
	{
		System.out.println("Server closed the connection.");
	}
	
	public int nextId()
	{
		return baseId + (currentId++ % maxIdCount);
	}
	
	private class KeyHandler extends KeyAdapter{

		@Override
		public void keyPressed(KeyEvent ke)
		{
			int keycode = ke.getKeyCode();
			if(keycode >= 37 && keycode <= 40){
				char[] keys = {'l', 'u', 'r', 'd'};	// Left, up (rotate), right, down
				char dir = keys[keycode-37];
				
				send(new TetrisSubmit(dir));
			}
		}

		@Override
		public void keyReleased(KeyEvent ke)
		{
		}

		@Override
		public void keyTyped(KeyEvent ke)
		{
		}		
	}
}
