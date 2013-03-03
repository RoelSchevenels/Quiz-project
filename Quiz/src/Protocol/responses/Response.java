package Protocol.responses;

import java.io.Serializable;

import network.Server;

public abstract class Response implements Serializable { 
	private static final long serialVersionUID = -8340575039709089557L;
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
