package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Receiver implements Runnable{
	ServerSocket serverSocket;
	String msg;
	
	public void run()
	{
		try {
			serverSocket = new ServerSocket(1337);
			ExecutorService ex = Executors.newCachedThreadPool();
			System.out.println("Wachten op verbinding...");
			while(true){
				Socket clientSocket = null;
				clientSocket = serverSocket.accept();
				ex.execute(new ConnectionWorker(clientSocket));
			}
			
		}catch(SocketException e){
			System.err.println("Verbinding verbroken.");
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Verbinding mislukt");
			System.exit(1);
			e.printStackTrace();
		}finally{
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}						
		}
	}
	private class ConnectionWorker implements Runnable{
		private final Socket socket;
		private boolean communicating;
		
		public ConnectionWorker(Socket socket)
		{
			this.socket = socket;
			communicating = true;
		}
		
		@Override
		public void run()
		{

			System.out.println("Verbinding met " + socket.getInetAddress().getHostAddress());
			ObjectInputStream in = null;
			try{
				in = new ObjectInputStream(socket.getInputStream());
				while(communicating){
					msg = in.readObject().toString();
					if(msg.equals("<done>")){
						communicating = false;
					}else{
						System.out.println(msg);
					}
				}
			}catch(IOException | ClassNotFoundException e){
				e.printStackTrace();
			}finally{
				try {
					socket.close();
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
