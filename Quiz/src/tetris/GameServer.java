package tetris;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import network.ConnectionWorker;

public class GameServer {
	Game game;
	ServerSocket serverSocket;
	Socket clientSocket;
	ExecutorService ex;
	Vector<ConnectionWorker> workers;
	int port;
	boolean online;
	int workerindex;
	Hashtable<Integer,String> roles;
	String activePlayer;
	
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

		public void handleData(Object data)
		{
			interpret(data, id);
		}

		public void handleDeath(int id)
		{
			removeThread(id);
		}		
	}
		
	// Interpreteten van ontvangen commando's.
	private synchronized void interpret(Object data, int id)
	{
		String cmd = (String)data;
		System.out.println(cmd);
		String param = "";
		if(cmd.contains(" ")){
			param = cmd.split(" ")[1];
			cmd = cmd.split(" ")[0];
		}
		
		System.out.println("cmd:"+cmd+";param:"+param);
		
		if(roles.containsKey(id) && roles.get(id).equals(activePlayer)){
			System.out.println("Correct");
			if(cmd.equals("down")){
				game.down();
			}else if(cmd.equals("right")){
				game.right();
			}else if(cmd.equals("left")){
				game.left();
			}else if(cmd.equals("rotate")){
				game.rotate();
			}
		}
		
		if(cmd.equals("start") && roles.get(id).equals("jury")){
			activePlayer = param;
			game.start(param);
		}else if(cmd.equals("init")){
			if (param.equals("player")){
				if (roles.contains("player1"))
					param = "player2";
				else
					param = "player1";
			}
			roles.put(id, param);
			System.out.println(param + " added");
		}
	}
	
	public GameServer()
	{
		port = 1337;
		this.online = true;
		clientSocket = null;
		workers = new Vector<ConnectionWorker>();
		game = new Game();
		roles = new Hashtable<Integer, String>();
		activePlayer="";
	}
	public GameServer(int port)
	{
		this.port = port;
	}
	
	public static void main(String arg[])
	{
		new GameServer().start();
	}

}
