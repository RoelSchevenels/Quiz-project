package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Protocol.SubmitManager;
import Protocol.exceptions.IdRangeException;
import Protocol.requests.Request;
import Protocol.responses.Response;
import Protocol.submits.IdRangeSubmit;
import Protocol.submits.Submit;
/**
 * @author Roel
 */
public class Client extends ConnectionWorker {
	private static Client instance;
	private static String serverIp;
	private static HashMap<Integer, Request> sentRequests = new HashMap<Integer, Request>();

	private int minReqId;
	private int maxReqId;
	private int curReqId;

	private Client(InetAddress ip) throws UnknownHostException, IOException
	{
		super(new Socket(ip, 1337), 0);
		ExecutorService ex = Executors.newCachedThreadPool();
		ex.execute(this);
		ex.shutdown();
	}

	public static Client getInstance() throws UnknownHostException, IOException
	{
		if (instance == null) {
			if(serverIp == null)
				instance = new Client(discover());
			else
				instance = new Client(InetAddress.getByName(serverIp));
		}
		return instance;
	}

	private static InetAddress discover() throws IOException
	{
		byte[] receiveData = new byte[50];
		DatagramSocket clientSocket = new DatagramSocket(1234);
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

		System.out.println("Waiting...");
		clientSocket.receive(receivePacket);
		clientSocket.close();
		System.out.println("Server IP: " + receivePacket.getAddress());
		return receivePacket.getAddress();
	}
	
	public static void setServerIp(String ip)
	{
		serverIp = ip;
	}

	public static void main(String arg[]) throws UnknownHostException, IOException
	{
		Client.getInstance();
	}

	public void handleInput(String input)
	{
	}

	@Override
	public void handleData(Object data)
	{
		System.out.println("Class " + data.getClass().toString());

		if (data instanceof IdRangeSubmit) {
			IdRangeSubmit irs = (IdRangeSubmit) data;

			minReqId = irs.getMin();
			maxReqId = irs.getMax();
			curReqId = minReqId;
			
		} else if (data instanceof Response) {
			Response r = (Response) data;
			if (sentRequests.containsKey(r.getRequestId())) {
				sentRequests.get(r.getRequestId()).fireResponse(r);
				sentRequests.remove(r.getRequestId());
			}
		} else if (data instanceof Submit) {
			SubmitManager.fireSubmit((Submit) data);
		}
	}

	/**
	 * Kleine toevoeging om requests te versturen
	 * 
	 * @param r
	 *            de request die gestuurd moet worden
	 * @author vrolijkx
	 */
	public void sendRequest(Request r)
	{
		sentRequests.put(r.getRequestId(), r);
		send(r);
		System.out.println("verzonden");
	}

	@Override
	public void handleDeath(int id)
	{
		System.out.println("Server closed the connection.");
	}

	public int nextId() throws IdRangeException
	{
		try {
			if (curReqId++ > maxReqId)
				curReqId = minReqId;
			return curReqId;
		} catch (NullPointerException e) {
			throw new IdRangeException(
					"Cannot generate ID: No ID range received from server");
		}
	}
}
