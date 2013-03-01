/**
 * @author vrolijkx;
 */
package javafFXpanels.ConnectToQuiz;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import Protocol.FxResponseListener;
import Protocol.exceptions.IdRangeException;
import Protocol.requests.ConnectToQuizRequest;
import Protocol.requests.GetQuizRequest;
import Protocol.requests.SuccesResponse;
import Protocol.responses.ExceptionResponse;
import Protocol.responses.GetQuizResponse;
import Protocol.responses.LoginResponse;
import Protocol.responses.Response;
import Protocol.responses.TeamLoginResponse;

import javaFXpanels.MessageProvider.LoadingPane;
import javaFXpanels.MessageProvider.MessageProvider;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Vrolijk Kristof <Vrolijkx.Kristof@gmail.com>
 */
public class ConnectToQuizController implements Initializable {
	@FXML
	private AnchorPane root;
	@FXML
	private Text quizText;

	
	private MessageProvider messageMaker;
	private LoadingPane loadingPane;
	private TeamLoginResponse team;
	private SimpleObjectProperty<GetQuizResponse> quis;
	private SimpleBooleanProperty ready;
	
	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		quis = new SimpleObjectProperty<GetQuizResponse>();
		ready = new SimpleBooleanProperty();
		messageMaker = new MessageProvider(root);
		loadingPane = new LoadingPane();
		initBinding();
		refresh();
	} 

	private void initBinding() {
		quizText.textProperty().bind(Bindings
				.createStringBinding(new Callable<String>() {

					@Override
					public String call() throws Exception {
						if(loadingPane.visibleProperty().get()) {
							return "";
						} else if(quis.get().equals(null)) {
							return "Geen quiz gevonden";
						} else {
							return quis.get().getQuizName();
						}
					}
				}, loadingPane.visibleProperty(), quis));
	}

	@FXML
	private void addToQuiz() {
		if(quis.get() == null) {
			messageMaker.showWarning("Geen quiz om op te conecteren");
			return;
		}
		
		ConnectToQuizRequest r;
		try {
			r = new ConnectToQuizRequest(team.getTeamId(),quis.get().getQuizId());
			r.onResponse(new FxResponseListener() {
				@Override
				public void handleFxResponse(Response response) {
					if(response instanceof ExceptionResponse) {
						messageMaker.showError(((ExceptionResponse) response).getExceptionMessage());
					} else if(response instanceof SuccesResponse) {
						ready.set(true);
					}
					loadingPane.hide();
				}
			});
			r.send();
			loadingPane.showLoading("Connecteren met quiz", root);
		} catch (IdRangeException | IOException e) {
			messageMaker.showError("Problemen met comunicatie met server");
		}

	}
	
	@FXML
	private void refresh() {
		GetQuizRequest r;
		try {
			r = new GetQuizRequest();
			r.onResponse(new FxResponseListener() {
				@Override
				public void handleFxResponse(Response response) {
					if(response instanceof ExceptionResponse) {
						messageMaker.showError(((ExceptionResponse) response).getExceptionMessage());
					} else if(response instanceof GetQuizResponse) {
						quis.set((GetQuizResponse) response);
					}
					loadingPane.hide();
				}
			});
			r.send();
			
			loadingPane.showLoading("Quis zoeken", root);
		} catch (IdRangeException | IOException e) {
			messageMaker.showError("Problemen met comunicatie met server");
		}

	}

	public void setTeamLogin(TeamLoginResponse login) {
		team = login;
	}
	
	public boolean isReady() {
		return ready.get();
	}
	
	public ReadOnlyBooleanProperty readyProperty() {
		return ready;
	}
}
