package Protocol.responses;

import java.io.Serializable;

import network.Server;

public abstract class Response implements Serializable { 
	protected int RequestId; //de id waarop de response een antwoord is
	
	public Response(int RequestId) {
		this.RequestId = RequestId;
	}
	
	public int getRequestId() {
		return RequestId;
	}
	
	public void send() {
		Server.getInstance().replyTo(this, RequestId);
	}
}
