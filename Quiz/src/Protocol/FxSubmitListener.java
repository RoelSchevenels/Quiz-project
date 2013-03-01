package Protocol;


import javafx.application.Platform;
import Protocol.submits.Submit;

/**
 * voert de submit uit op de FXaplication thread
 * @author vrolijkx
 */
public abstract class FxSubmitListener implements SubmitListener {

	@Override
	public void handleSubmit(final Submit submit) {
		Platform.runLater(new Runnable() {	
			@Override
			public void run() {
				handleFxSubmit(submit);
				
			}
		});
		
	}
	
	public abstract void handleFxSubmit(Submit submit);
	
}
