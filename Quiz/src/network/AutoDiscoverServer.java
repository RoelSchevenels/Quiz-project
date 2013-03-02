package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JFrame;
/**
 * @author Vrolijkx
 * @author Roel
 */
public class AutoDiscoverServer {
	private static AutoDiscoverServer instance;
	private boolean sending = false;
	private int port;
	private int defaultPort = 1234;

	public static AutoDiscoverServer getInstance()
	{
		if (instance == null) {
			instance = new AutoDiscoverServer();
		}
		return instance;
	}
	
	private AutoDiscoverServer() {
		this.port = defaultPort;
	};
	
	public void start() throws IOException {
		if(sending == false) {
			sending = true;
			ExecutorService ex = Executors.newSingleThreadExecutor();
			ex.execute(new sendloop());
			ex.shutdown();
		}
	}

	public void stop()
	{
		sending = false;
	}

	private class sendloop implements Runnable {

		@Override
		public void run()
		{
			DatagramSocket ss = null;

			try {
				ss = new DatagramSocket();
				ss.setBroadcast(true);

				byte[] bytes = new byte[100];

				DatagramPacket p = new DatagramPacket(bytes, bytes.length);

				// al host multicast address gebruiken
				p.setAddress(InetAddress.getByAddress(new byte[] { (byte)25, (byte)105, (byte)121, (byte)87 }));
				//p.setAddress(InetAddress.getByAddress(new byte[] {(byte) 127, (byte) 0, (byte) 0, (byte) 1}));
				p.setPort(port);
				p.setData(bytes);

				while (sending) {
					try {
						ss.send(p);
					} catch (IOException e1) {
						e1.printStackTrace();
					}

					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			} catch (UnknownHostException e2) {
				e2.printStackTrace();
			} catch (SocketException e2) {
				e2.printStackTrace();
			} finally {
				if (ss != null) {
					ss.close();
				}
			}

		}	
	}

	public int getPort()
	{
		return port;
	}

	public void setPort(int port)
	{
		this.port = port;
	}
	
	public static void main(String[] args)
	{
		try {
			AutoDiscoverServer.getInstance().start();
			JFrame frame = new JFrame("Server");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(100,100);
			frame.setVisible(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
