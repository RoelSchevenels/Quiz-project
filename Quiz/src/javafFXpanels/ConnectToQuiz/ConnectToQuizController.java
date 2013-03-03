/**
 * @author vrolijkx;
 */
package javafFXpanels.ConnectToQuiz;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javaFXpanels.MessageProvider.LoadingPane;
import javaFXpanels.MessageProvider.MessageProvider;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import Protocol.FxResponseListener;
import Protocol.exceptions.IdRangeException;
import Protocol.requests.ConnectToQuizRequest;
import Protocol.requests.GetQuizRequest;
import Protocol.requests.SuccesResponse;
import Protocol.responses.ExceptionResponse;
import Protocol.responses.GetQuizResponse;
import Protocol.responses.LoginResponse.UserType;
import Protocol.responses.Response;
import Protocol.responses.TeamLoginResponse;

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
	private UserType type;
	
	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		quis = new SimpleObjectProperty<GetQuizResponse>();
		ready = new SimpleBooleanProperty();
		messageMaker = new MessageProvider(root);
		loadingPane = new LoadingPane();
		refresh();
	} 


	@FXML
	private void addToQuiz() {
		System.out.println(type);
		if(quis.get() == null) {
			messageMaker.showWarning("Geen quiz om op te conecteren");
			return;
		} else if(type == null) {
			messageMaker.showWarning("Use setMode on this controller");
		} else if(type.equals(UserType.JURY)) {
			ready.set(true);
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
					loadingPane.hide();
					if(response instanceof ExceptionResponse) {
						messageMaker.showError(((ExceptionResponse) response).getExceptionMessage());
					} else if(response instanceof GetQuizResponse) {
						quis.set((GetQuizResponse) response);
						quizText.setText(((GetQuizResponse) response).getQuizName());
					} else {
						quizText.setText("Er is Voolopig geen\nQuiz om aan deel te nemen");
					}
					
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

	public void setMode(UserType t) {
		type = t;
	}
}
