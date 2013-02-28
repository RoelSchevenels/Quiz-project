package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AutoDiscoverServer {
	private static AutoDiscoverServer instance;
	private  boolean sending = false;
	private  String message;
	
	
	public static AutoDiscoverServer getInstance() {
		if(instance==null) {
			instance = new AutoDiscoverServer();
		}
		return instance;
	}
	
	private AutoDiscoverServer() {};
	
	public void start(String message) throws IOException {
		if(sending == false) {
			sending = true;
			ExecutorService ex = Executors.newSingleThreadExecutor();
			ex.execute(new sendloop());
		}	
	}
	
	public void stop() {
		sending = false;
	}
	
	private class sendloop implements Runnable {

		@Override
		public void run() {
			DatagramSocket ss = null;
			
			try{
			 ss = new DatagramSocket();
	         ss.setBroadcast(true);
	         
	         byte[] bytes = new byte[100];
	         //de message toevoegen
	         
	         DatagramPacket p = new DatagramPacket(bytes, bytes.length);
	         bytes = message.getBytes();
	         
	         //al host multicast address gebruiken
	         p.setAddress(InetAddress.getByAddress(new byte[] { (byte) 224,
	                 (byte) 0, (byte) 0, (byte) 1 }));
	         p.setPort(1333);
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
				if(ss!=null) {
					ss.close();
				} 
			}

			
		}
	}
}
