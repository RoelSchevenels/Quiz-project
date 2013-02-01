package test.git;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
	ServerSocket serverSocket;
	Socket clientSocket;
	ExecutorService ex;
	Vector<ConnectionWorker> workers;
	int port;
	boolean online;
	int workerindex;
	
	public void start()
	{
		try {
			workerindex = 0;
			serverSocket = new ServerSocket(port);
			ex = Executors.newCachedThreadPool();
			
			System.out.println("Wachten op verbindingen...");
			while(online){
				clientSocket = serverSocket.accept();
				workers.add(new ServerConnectionWorker(clientSocket, workerindex));
				ex.execute(workers.get(workerindex));
				workerindex++;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void removeThread(int index)
	{
		workers.remove(index);
		for(int i=index; i<workers.size();i++){
			workers.get(i).updateID(-1);
		}
	}
	
	private class ServerConnectionWorker extends ConnectionWorker{

		public ServerConnectionWorker(Socket sock, int id)
		{
			super(sock, id);
		}

		public void handleData(String data)
		{
			sendAll(data);
		}

		public void handleDeath(int id)
		{
			removeThread(id);
		}
		
	}
	
	private synchronized void sendAll(String message)
	{
		for(int i=0;i<workers.size();i++){
			workers.get(i).send(message);

			System.out.printf("sent \"%s\" to %d\n", message, i);
		}
	}
	
	public Server()
	{
		port = 1337;
		this.online = true;
		clientSocket = null;
		workers = new Vector<ConnectionWorker>();
	}
	public Server(int port)
	{
		this.port = port;
	}
	
	public static void main(String arg[])
	{
		new Server().start();
	}
}