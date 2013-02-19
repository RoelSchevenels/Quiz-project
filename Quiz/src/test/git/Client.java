package test.git;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Client extends ConnectionWorker{
		
	public Client() throws UnknownHostException, IOException
	{
		super(new Socket("127.0.0.1", 1337), 0);
	}
	
	public static void main(String arg[])
	{
		Scanner keyboard = new Scanner(System.in);
		ExecutorService ex = Executors.newCachedThreadPool();
		Client client = null;
		try {
			client = new Client();
		} catch (UnknownHostException e) {
			System.out.println("Could not find host");
		} catch (IOException e) {
			e.printStackTrace();
		}
		ex.execute(client);
		while(keyboard.hasNextLine()){
			client.handleInput(keyboard.nextLine());
		}
		System.out.println("Closing input");
		keyboard.close();
	}

	public void handleInput(String input)
	{
		if(input.matches("^(S|s)end (I|i)mage .*")){
			String path = input.substring(11);
			System.out.println("Opening " + path);
			try {
				BufferedImage image = ImageIO.read(new File(path));
				ImageIcon img = new ImageIcon(image);
				this.send(img);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			this.send(input);
		}
	}

	@Override
	public void handleData(Object data)
	{

		System.out.println("Class " + data.getClass().toString());
		if(data.getClass().toString().equals("class javax.swing.ImageIcon")){
			new ImageViewer((ImageIcon) data);
		}else{
			System.out.println(data);
		}
	}


	@Override
	public void handleDeath(int id)
	{
		System.out.println("Server closed the connection.");
	}
}
