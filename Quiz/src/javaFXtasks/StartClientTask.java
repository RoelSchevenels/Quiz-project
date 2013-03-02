package javaFXtasks;

import java.io.IOException;
import java.net.UnknownHostException;

import javafx.concurrent.Task;
import network.Client;
/**
 * @author Roel
 */
@SuppressWarnings("rawtypes")
public class StartClientTask extends Task {
	private static String host;
	public StartClientTask()
	{
	}
	
	public StartClientTask(String serverIp)
	{
		host = serverIp;
	}
	
	@Override
	protected Object call() throws UnknownHostException, IOException
	{
		if(host == null) {
			Client.getInstance();
		} else {
			Client.getInstance(host);
		}
		return null;
	}
}
