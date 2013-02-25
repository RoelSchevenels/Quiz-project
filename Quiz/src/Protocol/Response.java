package Protocol;

import java.io.Serializable;

public abstract class Response implements Serializable { 
	protected int RequestId; //de id waarop de response een antwoord is
	
	public Response(int RequestId) {
		this.RequestId = RequestId;
	}
	
	public int getRequestId() {
		return RequestId;
	}
	
	public void send() {
		//TODO: zorgen dat de response terug wordt verzonden waar de request vandaan kwam
	}
}
