package test.git;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Receiver {
	ServerSocket serverSocket;
	Socket connection;
	ObjectInputStream in;
	String msg;
	
	public void run()
	{
		try {
			serverSocket = new ServerSocket(1337);
			System.out.println("Wachten op verbinding...");
			connection = serverSocket.accept();
			System.out.println("Verbinding met " + connection.getInetAddress().getHostAddress());
			boolean communicating = true;
			in = new ObjectInputStream(connection.getInputStream());
			while(communicating){
				msg = in.readObject().toString();
				if(msg.equals("<done>")){
					communicating = false;
				}else{
					System.out.println(msg);
				}
			}
			
		} catch (IOException e) {
			System.err.println("Verbinding mislukt");
			System.exit(1);
		} catch (ClassNotFoundException e) {
			System.err.println("Data in onbekend formaat");
		}finally{
			try {
				serverSocket.close();
				connection.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}						
		}
	}
	
	public static void main(String arg[])
	{
		new Receiver().run();
	}
}
