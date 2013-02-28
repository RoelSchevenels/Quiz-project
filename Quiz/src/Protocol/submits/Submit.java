package Protocol.submits;

import java.io.IOException;
import java.io.Serializable;
import java.net.UnknownHostException;

import network.Client;
/**
 * @author Roel
 */
public abstract class Submit implements Serializable {
	private static final long serialVersionUID = -7753040479764135232L;

	public void send() throws UnknownHostException, IOException
	{
		Client.getInstance().send(this);
	}
}
