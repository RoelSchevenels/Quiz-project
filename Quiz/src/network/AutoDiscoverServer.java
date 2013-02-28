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

import org.h2.constant.SysProperties;

public class AutoDiscoverServer {
	private static AutoDiscoverServer instance;
	private boolean sending = false;
	private String message;

	public static AutoDiscoverServer getInstance()
	{
		if (instance == null) {
			instance = new AutoDiscoverServer();
		}
		return instance;
	}

	private AutoDiscoverServer()
	{
	};

	public void start() throws IOException
	{
		System.out.println("start");
		if (sending == false) {
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
				// de message toevoegen

				DatagramPacket p = new DatagramPacket(bytes, bytes.length);

				// al host multicast address gebruiken
				//p.setAddress(InetAddress.getByAddress(new byte[] { (byte) 224,(byte) 0, (byte) 0, (byte) 1 }));
				p.setAddress(InetAddress.getByAddress(new byte[] {
						(byte) 127, (byte) 0, (byte) 0, (byte) 1
				}));
				p.setPort(1333);
				p.setData(bytes);

				while (sending) {
					try {
						ss.send(p);
						System.out.println("Sent " + p.getData());
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