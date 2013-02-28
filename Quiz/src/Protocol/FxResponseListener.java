package Protocol;


import javafx.application.Platform;
import Protocol.responses.Response;

/**
 * voert de response uit op de FXaplication thread
 * @author vrolijkx
 */
public abstract class FxResponseListener implements ResponseListener {

	@Override
	public void handleResponse(final Response response) {
		Platform.runLater(new Runnable() {	
			@Override
			public void run() {
				handleFxResponse(response);
				
			}
		});
		
	}
	
	public abstract void handleFxResponse(Response response);
	
}
