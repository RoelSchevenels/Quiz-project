package Protocol;

import Protocol.responses.Response;

public interface  ResponseListener {
	public void handleResponse(Response response);
}
