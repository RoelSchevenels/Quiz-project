package Protocol.responses;

import java.io.Serializable;

import network.Server;

public abstract class Response implements Serializable { 
	protected int requestId; //de id waarop de response een antwoord is
	
	public Response(int RequestId) {
		this.requestId = RequestId;
	}
	
	public int getRequestId() {
		return requestId;
	}
	
	public void send() {
		Server.getInstance().replyTo(this, requestId);
	}
}
