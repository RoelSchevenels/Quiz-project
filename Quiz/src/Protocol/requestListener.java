package Protocol;

import Protocol.requests.Request;


public interface requestListener {
	public void handleRequest(Request r);
}
