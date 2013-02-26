package network;

import java.io.IOException;
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

public class Client extends ConnectionWorker{
	private  static Client instance;
	private  static HashMap<Integer, Request> sendRequests = new HashMap<Integer,Request>();
	
	private int minReqId;
	private int maxReqId;
	private int curReqId;
	
		
	private Client() throws UnknownHostException, IOException
	{
		super(new Socket("127.0.0.1", 1337), 0);
	}
	
	public static Client getInstance()
	{
		//TODO: ge moet die exceptions doorgooien
		if(instance == null){
			try {
				instance = new Client();
			} catch (UnknownHostException e) {
				System.out.println("Kan server niet vinden");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return instance;
	}
	
	public static void main(String arg[])
	{
		ExecutorService ex = Executors.newCachedThreadPool();
		ex.execute(getInstance());
		ex.shutdown();
	}

	public void handleInput(String input)
	{
	}

	@Override
	public void handleData(Object data)
	{
		System.out.println("Class " + data.getClass().toString());
		
		if(data instanceof IdRangeSubmit){
			IdRangeSubmit irs = (IdRangeSubmit)data;
			
			minReqId = irs.getMin();
			maxReqId = irs.getMax();
			curReqId = minReqId;
		} else if(data instanceof Response) {
			Response r = (Response) data;
			if(sendRequests.containsKey(r.getRequestId())) {
				sendRequests.get(r.getRequestId()).fireResponse(r);
				sendRequests.remove(r.getRequestId());
			}
		} else if(data instanceof Submit) {
			SubmitManager.fireSubmit((Submit) data);
		}
	}

	/**
	 * Kleine toevoeging om requests te versturen
	 * @param r de request die gestuurd moet worden
	 * @author vrolijkx
	 */
	public void sendRequest(Request r) {
		sendRequests.put(r.getRequestId(), r);
		send(r);
	}
	
	@Override
 	public void handleDeath(int id)
	{
		System.out.println("Server closed the connection.");
	}
	
	public int nextId() throws IdRangeException
	{
		try{
			if(curReqId++ > maxReqId)
				curReqId = minReqId;
			return curReqId;
		}catch(NullPointerException e){
			throw new IdRangeException("Cannot generate ID: No ID range received from server");
		}
	}
}
