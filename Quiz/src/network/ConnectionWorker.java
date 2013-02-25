package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public abstract class ConnectionWorker implements Runnable{
	private final Socket socket;
	private boolean communicating;
	private Object toSend;
	private int refreshrate;
	protected int id;
	
	public ConnectionWorker(Socket sock, int id)
	{
		this.socket = sock;
		this.communicating = true;
		this.refreshrate = 100;
		this.toSend = "";
		this.id = id;
	}
	
	public void run()
	{
		System.out.printf("Verbinding met %s gemaakt.\n", socket.getInetAddress().getHostAddress());
		ObjectInputStream in = null;
		ObjectOutputStream out = null;
		try {
			socket.getInputStream();
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			while(communicating){
				if(!toSend.equals("")){	//Als er een bericht te sturen is
					//System.out.println("Going to write " + toSend);
					out.writeObject(toSend);
					out.flush();
					//System.out.println("flushed " + toSend);
					toSend = "";
				}
				if(socket.getInputStream().available() > 0 ){
					Object msg = in.readObject();
					System.out.println(msg.getClass().toString());
					//System.out.printf("\"%s\" received\n", msg);
					handleData(msg);
				}
				Thread.sleep(refreshrate);
			}
			
		}catch(SocketException e){
			System.out.println("This connection died. :(");
			Thread.currentThread().interrupt();
			handleDeath(id);
		} catch (IOException | ClassNotFoundException | InterruptedException e) {
			e.printStackTrace();
		} finally{
			try {
				if(in != null)
					in.close();
				if(out != null)
					out.close();
			} catch (IOException e) {
				// Lege catch omdat het toch al gesloten is, dus eigenlijk is alles in orde.
			}
		} 
	}
	
	public synchronized void send(Object data)
	{
		this.toSend = data;
	}
	public synchronized void updateID(int change)
	{
		this.id += change;
	}
	public abstract void handleData(Object data);
	public abstract void handleDeath(int id);
}
