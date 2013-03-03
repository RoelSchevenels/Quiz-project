package network;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JFrame;

import Protocol.RequestManager;
import Protocol.SubmitManager;
import Protocol.requests.Request;
import Protocol.submits.IdRangeSubmit;
import Protocol.submits.IdentifiedSubmit;
import Protocol.submits.Submit;
import Util.ConnectionUtil;
/**
 * @author Roel
 */
public class Server {
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
	private ArrayList<Integer> jury;
	private Hashtable<Integer, Integer> requests;

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
		this.jury = new ArrayList<Integer>();
		this.playingTeams = new ArrayList<Integer>();
		Executors.newSingleThreadExecutor().execute(new Runnable() {
			
			@Override
			public void run() {
				start();
			}
		});
		
		Mapper.mapRequests();
		Mapper.mapSubmits();
	}

	public static Server getInstance(int port)
	{
		if (instance == null) {
			instance = new Server(port);
		}
		return instance;
	}

	public static Server getInstance()
	{
		if (instance == null) {
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
			while (online) {
				clientSocket = serverSocket.accept();
				workers.add(new ServerConnectionWorker(clientSocket,workerindex));
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
		for (int i = index; i < workers.size(); i++) {
			workers.get(i).updateID(-1);
		}
	}

	@SuppressWarnings("unused")
	private synchronized void sendAll(Object data)
	{
		for (int i = 0; i < workers.size(); i++) {
			workers.get(i).send(data);

			System.out.printf("sent \"%s\" to %d\n", data, i);
		}
	}

	// Naar allen behalve degene die het zelf gestuurd heeft
	@SuppressWarnings("unused")
	private synchronized void sendAll(Object data, int except)
	{
		for (int i = 0; i < workers.size(); i++) {
			if (i != except) {
				workers.get(i).send(data);

				System.out.printf("sent \"%s\" to %d\n", data, i);
			}
		}
	}

	public synchronized void sendToPlayers(Object data)
	{
		for (int id : players) {
			workers.get(id).send(data);
			System.out.printf("sent \"%s\" to %d\n", data, id);
		}
	}

	public synchronized void sendToJury(Object data)
	{
		for (int id : jury) {
			workers.get(id).send(data);
			System.out.printf("sent \"%s\" to %d\n", data, id);
		}
	}

	public synchronized void sendToTeams(Object data)
	{
		for (int id : playingTeams) {
			workers.get(id).send(data);
			System.out.printf("sent \"%s\" to %d\n", data, id);
		}
	}

	public synchronized void replyTo(Object data, int requestId)
	{
		System.out.println("terug sturen");
		int workerId = requests.get(requestId);
		workers.get(workerId).send(data);
		requests.remove(requestId);
	}

	private synchronized void interpret(Object data, int id)
	{
		System.out.println("data komt binnen");
		if (data instanceof Submit) {
			if (data instanceof IdentifiedSubmit) {
				((IdentifiedSubmit)data).setConnectionId(id);
			}
			SubmitManager.fireSubmit((Submit) data);
		} else if (data instanceof Request) {
			Request r = (Request) data;
			System.out.println("het is een request");
			requests.put(r.getRequestId(), id);
			RequestManager.fireRequest(r);
		}
	}

	public void markRequestAsJury(int requestId)
	{
		jury.add(requests.get(requestId));
	}

	public void markRequestAsPlayer(int requestId)
	{
		players.add(requests.get(requestId));
	}

	public void markRequestAsPlayingTeam(int requestId)
	{
		playingTeams.add(requests.get(requestId));
	}

	private class ServerConnectionWorker extends ConnectionWorker {

		public ServerConnectionWorker(Socket sock, int id)
		{
			super(sock, id);
			send(new IdRangeSubmit(id * 100, id * 100 + 99));
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
	
	public int getPort()
	{
		return this.port;
	}

	//test die database opstart en autodiscovery
	public static void main(String arg[])
	{
		ConnectionUtil.StartDataBase();
		
		Server.getInstance();
		
		try {
			AutoDiscoverServer.getInstance().start();
			JFrame f = new JFrame();
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			f.setVisible(true);
			f.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					ConnectionUtil.CloseSessionFactory();
					super.windowClosing(e);
				}
			});
			f.setSize(500, 500);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
