package Protocol;

import Protocol.responses.Response;

public interface  responseListener {
	public void handleResponse(Response response);
}
