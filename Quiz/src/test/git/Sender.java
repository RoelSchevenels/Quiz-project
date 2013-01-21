package test.git;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class Sender implements Runnable{
	Socket socket;
	ObjectOutputStream out;
	String target;
	int port;
	String sentence;
	String name;
	
	public Sender()
	{
		this("127.0.0.1", 1337);
	}
	
	public Sender(String target, int port)
	{
		this.target = target;
		this.port = port;
	}
	
	public Sender(String target, int port, String name)
	{
		this(target, port);
		this.name = name;
	}
	
	public void run()
	{
		String[] message;
		if(sentence == null){
			message = "I don't know why you say goodbye I say hello <done>".split(" ");
		}else{
			message = sentence.split(" ");
		}
		try{
			socket = new Socket(target, port);
			out = new ObjectOutputStream(socket.getOutputStream());
			for(String s: message){
				Thread.sleep(500);
				sendMessage(s);
			}
		}catch(IOException e){
			System.err.println("Verbinding mislukt.");
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	void sendMessage(String msg)
	{
		if (!(name.equals("") || msg.equals("<done>")))
			msg = name + ": " + msg;
			
		try{
			out.writeObject(msg);
			out.flush();
		}
		catch(SocketException e){
			System.err.println("Verbinding verbroken.");
			System.exit(1);
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	
	public void setSentence(String sentence)
	{
		this.sentence = sentence;
	}
}

	