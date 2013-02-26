package Protocol.requests;

import java.io.Serializable;

import network.Client;

import Protocol.ResponseListener;
import Protocol.exceptions.IdRangeException;
import Protocol.responses.ExceptionResponse;
import Protocol.responses.Response;

/**
 * een aanvraag aan de server voor een bepaalde functie
 * @author vrolijkx
 */
public abstract class Request implements Serializable {
	private static final long serialVersionUID = 8950145620171342174L;
	protected transient ResponseListener responseHandler;
	protected int requestId;
	


	public Request() throws IdRangeException {
		this.requestId = Client.getInstance().nextId();
	}
	
	/**
	 * maak een lege response aan voor deze request
	 * @return de bijpassende response
	 */
	public abstract Response createResponse();
	
	
	/**
	 * wat moet er gebeure als er een response op deze request terug komt
	 * @param listener de gene die naar de response luisterd
	 */
 	public void onResponse(ResponseListener listener) {
		responseHandler = listener;
	}
	
	public void fireResponse(Response r) {
		if(responseHandler != null) {
			responseHandler.handleResponse(r);
		}
	}

	public void send() {
		Client.getInstance().sendRequest(this);
	}
	
	/**
	 * answer de request met exception
	 * @param message
	 */
	public void sendException(String message) {
		ExceptionResponse ex  =  new ExceptionResponse(requestId,message);
		ex.send();
	}
	
	public int getRequestId() {
		return requestId;
	}
	
}
