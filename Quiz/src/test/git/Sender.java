package test.git;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Sender {
	Socket socket;
	ObjectOutputStream out;
	
	public void run(String target)
	{
		String[] message = "Hello hello I don't know why you say goodbye I say hello <done>".split(" ");
		try{
			socket = new Socket(target, 1337);
			out = new ObjectOutputStream(socket.getOutputStream());
			for(String s: message){
				Thread.sleep(500);
				sendMessage(s);
			}
		}catch(IOException e){
			System.err.println("Verbinding mislukt.");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	void sendMessage(String msg)
	{
		try{
			out.writeObject(msg);
			out.flush();
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	
	public static void main(String arg[])
	{
		String target = (arg.length>1) ? arg[1] : "127.0.0.1";
		new Sender().run(target);
	}
}
