package test.git;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
			client.send(keyboard.nextLine());
		}
		System.out.println("Closing input");
		keyboard.close();
	}



	@Override
	public void handleData(String data)
	{
		System.out.println(data);
	}



	@Override
	public void handleDeath(int id)
	{
		System.out.println("Server closed the connection.");
	}
}
