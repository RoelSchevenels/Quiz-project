package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Protocol.RequestListener;
import Protocol.RequestManager;
import Protocol.SubmitListener;
import Protocol.SubmitManager;
import Protocol.requests.LoginRequest;
import Protocol.requests.Request;
import Protocol.responses.LoginResponse.UserType;
import Protocol.submits.IdRangeSubmit;
import Protocol.submits.Submit;
import Protocol.submits.TetrisSubmit;
import Util.DatabaseUtil;

//TODO: send to players,jurys,playingTeams
public class Server {
	//Foei, foei,foei roel al u privates vergeten
	private static Server instance;
	private ServerSocket serverSocket;
	private Socket clientSocket;
	private ExecutorService ex;
	private Vector<ConnectionWorker> workers;
	private int port;
	private boolean online;
	private int workerindex;
	private ArrayList<Integer> playingTeams;
	private ArrayList<Integer> players;
	private ArrayList<Integer> jurys;
	private Hashtable<Integer,Integer> requests;
	
	private Server()
	{
		this(1337);
	}
	
	private Server(int port)
	{
		this.port = port;
		this.online = true;
		this.clientSocket = null;
		this.workers = new Vector<ConnectionWorker>();
		this.requests = new Hashtable<Integer, Integer>();
		this.players = new ArrayList<Integer>();
		this.jurys = new ArrayList<Integer>();
		this.playingTeams = new ArrayList<Integer>();
		mapRequests();
		mapSubmits();
	}
	
	public static Server getInstance(int port)
	{
		if(instance == null){
			instance = new Server(port);
		}
		return instance;
	}
	
	public static Server getInstance() {
		if(instance == null){
			instance = new Server();
		}
		return instance;
	}
	
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
	
	@SuppressWarnings("unused")
	private synchronized void sendAll(Object data)
	{
		for(int i=0;i<workers.size();i++){
			workers.get(i).send(data);

			System.out.printf("sent \"%s\" to %d\n", data, i);
		}
	}
	
	// Naar allen behalve degene die het zelf gestuurd heeft
	@SuppressWarnings("unused")
	private synchronized void sendAll(Object data, int except)
	{
		for(int i=0;i<workers.size();i++){
			if(i != except){
				workers.get(i).send(data);
		
				System.out.printf("sent \"%s\" to %d\n", data, i);
			}
		}
	}
	
	public synchronized void replyTo(Object data, int requestId)
	{
		int workerId = requests.get(requestId);
		workers.get(workerId).send(data);
		//TODO: de request uit de hash verwrijderen
		requests.remove(requestId);
	}
	
	private synchronized void interpret(Object data, int id)
	{
		if(data instanceof Submit) {
			SubmitManager.fireSubmit((Submit) data);
		}else if(data instanceof Request) {
			Request r = (Request)data;
			requests.put(r.getRequestId(),id);
			RequestManager.fireRequest(r);
		}
	}
	
	
	public void markRequestAsJurry(int requestId) {
		jurys.add(requests.get(requestId));
	}
	
	public void markRequestAsPlayer(int requestId) {
		players.add(requests.get(requestId));
	}
	
	public void markRequestAsPlayingTeam(int requestId) {
		playingTeams.add(requests.get(requestId));
	}
	
	private void mapRequests()
	{
		RequestManager.addRequestListener(LoginRequest.class, new RequestListener() {
			public void handleRequest(Request r)
			{
				DatabaseUtil.handleLoginRequest((LoginRequest) r);	
			}
		});
	}
	
	private void mapSubmits()
	{
		SubmitManager.addSubmitListener(TetrisSubmit.class, new SubmitListener() {
			public void handleSubmit(Submit r)
			{
				// TODO Auto-generated method stub
				
			}
		});
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

	public static void main(String arg[])
	{
		getInstance().start();
	}
}
