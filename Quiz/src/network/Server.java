package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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
import Protocol.submits.IdRangeSubmit;
import Protocol.submits.Submit;
import Protocol.submits.TetrisSubmit;

public class Server {
	ServerSocket serverSocket;
	Socket clientSocket;
	ExecutorService ex;
	Vector<ConnectionWorker> workers;
	int port;
	boolean online;
	int workerindex;
	Hashtable<Integer,String> roles;
	Hashtable<Integer,Integer> requests;
	static Server instance;
	
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
	}
	
	private synchronized void interpret(Object data, int id)
	{
		if(data instanceof Submit) {
			// TODO: Doorgeven aan SubmitManager
		}else if(data instanceof Request) {
			Request r = (Request)data;
			requests.put(r.getRequestId(),id);
			RequestManager.fireRequest(r);
		}
	}
	
	private Server()
	{
		port = 1337;
		this.online = true;
		clientSocket = null;
		workers = new Vector<ConnectionWorker>();
		roles = new Hashtable<Integer, String>();
		requests = new Hashtable<Integer, Integer>();
		mapRequests();
		mapSubmits();
	}
	
	private Server(int port)
	{
		this.port = port;
	}
	
	public static Server getInstance()
	{
		if(instance == null){
			instance = new Server();
		}
		return instance;
	}
	
	public static void main(String arg[])
	{
		getInstance().start();
	}
	
	private void mapRequests()
	{
		RequestManager.addRequestListener(LoginRequest.class, new RequestListener() {
			public void handleRequest(Request r)
			{
				// TODO Auto-generated method stub
				
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
}
