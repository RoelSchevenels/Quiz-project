package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ArrayBlockingQueue;

public abstract class ConnectionWorker implements Runnable {
	private final Socket socket;
	private boolean communicating;
	private ArrayBlockingQueue<Object> queue;
	private int refreshrate;
	protected int id;

	public ConnectionWorker(Socket sock, int id)
	{
		this.socket = sock;
		this.communicating = true;
		this.refreshrate = 100;
		this.queue = new ArrayBlockingQueue<Object>(20);
		this.id = id;
	}

	public void run()
	{
		System.out.printf("Verbinding met %s gemaakt.\n", socket
				.getInetAddress().getHostAddress());
		ObjectInputStream in = null;
		ObjectOutputStream out = null;
		try {
			socket.getInputStream();
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			while (communicating) {
				if (queue.size() > 0) { // Als er een bericht te sturen is
					out.writeObject(queue.take());
					out.flush();
				}
				if (socket.getInputStream().available() > 0) {
					Object msg = in.readObject();
					handleData(msg);
				}
				Thread.sleep(refreshrate);
			}

		} catch (SocketException e) {
			System.out.println("This connection has died.");
			Thread.currentThread().interrupt();
			handleDeath(id);
		} catch (IOException | ClassNotFoundException | InterruptedException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close();
				if (out != null)
					out.close();
			} catch (IOException e) {
				// Lege catch omdat het toch al gesloten is, dus eigenlijk is
				// alles in orde.
			}
		}
	}

	public synchronized void send(Object data)
	{		
		try {
			this.queue.put(data);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public synchronized void updateID(int change)
	{
		this.id += change;
	}

	public abstract void handleData(Object data);

	public abstract void handleDeath(int id);
}
