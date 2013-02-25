package tetris;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import network.ConnectionWorker;
import Protocol.AuthSubmit;
import Protocol.IdRangeSubmit;
import Protocol.TetrisStartSubmit;
import Protocol.TetrisSubmit;

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
			send(new IdRangeSubmit(id*100, id*100 + 99));
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
		if(data instanceof TetrisSubmit) {
			if(roles.containsKey(id) && roles.get(id).equals(activePlayer)) {
				char dir = ((TetrisSubmit)data).getMovement();
				switch(dir){
				case 'l':
					game.left();
					break;
				case 'd':
					game.down();
					break;
				case 'r':
					game.right();
					break;
				case 'u':
					game.rotate();
					break;
				}
			}
		}else if(data instanceof TetrisStartSubmit) {
			if(roles.get(id).equals("jury")){
				TetrisStartSubmit tss = (TetrisStartSubmit)data; 
				activePlayer = tss.getPlayer();
				int pieces = tss.getPieces();
				game.start(activePlayer, pieces);
			}
		}else if(data instanceof AuthSubmit) {
			String role = ((AuthSubmit)data).getRole();
			if(role.equals("player")) {
				// Als de rol player is, er player1 en player2 van maken.
				role = (roles.contains("player1")) ? "player2" : "player1";
			}
			roles.put(id, role);
			System.out.printf("User with role %s connected\n",role);
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
