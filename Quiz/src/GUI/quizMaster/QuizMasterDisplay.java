/**
 * de display van de quizmaster tijdens een quiz
 * @author vrolijkx
 */
package GUI.quizMaster;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javaFXpanels.MediaPane.MediaControllerController;
import javaFXpanels.MessageProvider.MessageProvider;

import screenManger.ScreenManeger;

import javafx.animation.FadeTransition;
import javafx.animation.FadeTransitionBuilder;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ParallelTransitionBuilder;
import javafx.animation.RotateTransition;
import javafx.animation.RotateTransitionBuilder;
import javafx.animation.TranslateTransition;
import javafx.animation.TranslateTransitionBuilder;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.ScrollPaneBuilder;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.Duration;

import BussinesLayer.QuestionRound;
import BussinesLayer.Quiz;
import BussinesLayer.questions.MultipleChoise;
import BussinesLayer.questions.MusicQuestion;
import BussinesLayer.questions.PictureQuestion;
import BussinesLayer.questions.Question;
import BussinesLayer.questions.VideoQuestion;
import BussinesLayer.resources.Resource;



public class QuizMasterDisplay extends AnchorPane{
	public static double MAX_SLIDEBAR_OPACITY = 0.8;
	private SimpleObjectProperty<Question> currentQuestion;
	private SimpleObjectProperty<QuestionRound> currentRound;
	private SimpleObjectProperty<Resource> currentResource; 
	private int roundNumber;
	private List<QuestionRound> rounds;
	private List<Question> questions;
	private Quiz quiz;

	//visual components
	private Pagination questionNavigator;
	private AnchorPane slideInPane;
	private StackPane slideInBar;
	private ImageView slideInImage;
	private ListView<QuestionRound> roundsList;
	private MessageProvider messageMaker;

	//animations
	private RotateTransition rotateAnimation;
	private FadeTransition fadeAnimation;
	private TranslateTransition translateAnimation;
	private ParallelTransition slideInAnimations;
	private boolean slidedOut;

	public QuizMasterDisplay(Quiz q) {
		quiz = q;
		rounds = quiz.getRounds();
		this.currentRound = new SimpleObjectProperty<QuestionRound>();
		this.currentQuestion = new SimpleObjectProperty<Question>();
		this.currentResource = new SimpleObjectProperty<Resource>();
		roundNumber = 0;

		initComponents();
		initActions();
		setDefaultStyle();
		messageMaker = new MessageProvider(this);

		if(rounds.size() > 0) {
			this.setQuestionRound(rounds.get(roundNumber));
		} else {
			questionNavigator.setPageCount(2);
		}
		
	}

	private void setDefaultStyle() {
		this.getStyleClass().add("quizmaster-display");
		this.questionNavigator.getStyleClass().add("question-pagiation");
		this.slideInPane.getStyleClass().add("slide-in-pane");
		this.slideInBar.getStyleClass().add("slide-in-bar");
		this.slideInImage.getStyleClass().add("slide-in-image");
		this.roundsList.getStyleClass().add("slide-in-list");
		//en default laden op de root

		this.getStylesheets().add(this.getClass().getResource("quizDisplay.css").toString());

	}

	//de animaties en acties instellen
	private void initActions() {
		//pageFactory zetten deze geeft de content terug
		questionNavigator.setPageFactory(new Callback<Integer, Node>() {
			@Override
			public Node call(Integer param) {
				return createPane(param);
			}
		});

		//wanneer de volgende ronde laden
		questionNavigator.currentPageIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				int size = questions == null ? 0 : questions.size();
				if(newValue.intValue() == size + 1 && roundNumber < rounds.size() - 1) {
					setQuestionRound(rounds.get(++roundNumber));
				}
				
			}
		});

		//uitschuiven van de zijkant bij klik of spatie
		slideInBar.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				togleSlideIn();	
			}
		});

		this.roundsList.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if(event.getCode().equals(KeyCode.SPACE)) {
					togleSlideIn();
					event.consume();
				}
			}
		});

		this.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if(event.getCode().equals(KeyCode.SPACE)) {
					togleSlideIn();
					event.consume();
				}
			}
		});

		//de uitschuif pijl weergeven of verbergen
		slideInBar.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				playShow();
			}
		});

		slideInBar.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				playHide();
			}
		});

		//de ronde waarop geklikt is weergeven
		roundsList.getSelectionModel().getSelectedItems()
		.addListener(new ListChangeListener<QuestionRound>() {
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends QuestionRound> c) {
				setQuestionRound(roundsList.getSelectionModel().getSelectedItem());
			}

		});

		//animaties aanmaken
		fadeAnimation = FadeTransitionBuilder.create()
				.node(slideInBar)
				.duration(Duration.millis(300))
				.build();

		translateAnimation = TranslateTransitionBuilder.create()
				.node(slideInPane)
				.duration(Duration.millis(750))
				.build();

		rotateAnimation = RotateTransitionBuilder.create()
				.node(slideInImage)
				.duration(Duration.millis(750))
				.build();

		slideInAnimations = ParallelTransitionBuilder.create()
				.interpolator(Interpolator.EASE_BOTH)
				.children(translateAnimation,rotateAnimation)
				.build();

		slidedOut = false;
	}

	private void initComponents() {
		//pagiation aanmaken en stijl zetten
		questionNavigator = new Pagination(); 
		questionNavigator.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);
		questionNavigator.setStyle("-fx-page-information-visible: false;");

		AnchorPane.setBottomAnchor(questionNavigator, 0.0);
		AnchorPane.setLeftAnchor(questionNavigator, 0.0);
		AnchorPane.setRightAnchor(questionNavigator, 0.0);
		AnchorPane.setTopAnchor(questionNavigator, 0.0);

		//linker Slidein maken
		slideInPane = new AnchorPane();
		slideInPane.setPrefWidth(200.0);
		slideInPane.setMaxWidth(200.0); //mss in achteraf dynamische breete toestaan
		slideInPane.setTranslateX(-200);
		slideInPane.setVisible(true);

		//clip voor slidePane aanmaken
		Rectangle r = new Rectangle();
		r.widthProperty().bind(this.widthProperty());
		r.heightProperty().bind(this.heightProperty());
		slideInPane.setClip(r);

		AnchorPane.setTopAnchor(slideInPane, 5.0);
		AnchorPane.setBottomAnchor(slideInPane, 5.0);
		AnchorPane.setLeftAnchor(slideInPane, 0.0);


		//lijst aanmaken en stijlen
		roundsList = new ListView<QuestionRound>();
		roundsList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		roundsList.getItems().addAll(rounds);
		//om problemen met resizen te voorkomen
		roundsList.minWidthProperty().bind(slideInPane.widthProperty());
		roundsList.minHeightProperty().bind(slideInPane.heightProperty());
		roundsList.maxHeightProperty().bind(slideInPane.maxHeightProperty());

		AnchorPane.setBottomAnchor(roundsList, 0.0);
		AnchorPane.setLeftAnchor(roundsList, 0.0);
		AnchorPane.setRightAnchor(roundsList, 0.0);
		AnchorPane.setBottomAnchor(roundsList, 0.0);

		//image maken
		Image i = new Image(this.getClass().getResourceAsStream("nav_right.png"));

		//image en bar aanmaken
		slideInImage = new ImageView();
		slideInImage.setImage(i);
		//zorgen dat image niet geschaald kan worden
		slideInImage.setFitWidth(i.getWidth());
		slideInImage.setFitHeight(i.getHeight());

		slideInBar = new StackPane();

		//zo breed als de afbeelding zetten
		slideInBar.setPrefWidth(i.getWidth());
		slideInBar.setMinWidth(i.getWidth());
		slideInBar.setMaxWidth(i.getWidth());
		slideInBar.minHeightProperty().bind(slideInPane.heightProperty());
		slideInBar.setOpacity(0.0);

		StackPane.setAlignment(slideInImage, Pos.CENTER);

		AnchorPane.setBottomAnchor(slideInBar, 0.0);
		AnchorPane.setBottomAnchor(slideInBar, 0.0);
		AnchorPane.setRightAnchor(slideInBar, -slideInBar.getPrefWidth());

		slideInBar.getChildren().add(slideInImage);
		slideInPane.getChildren().addAll(roundsList,slideInBar);

		this.getChildren().addAll(questionNavigator,slideInPane);
		
		this.setPrefSize(600.0, 500.0);
	};

	private Node createPane(Integer param) {
		//ScrollPane en de box maken
		final ScrollPane sPane = ScrollPaneBuilder.create()
				.hbarPolicy(ScrollBarPolicy.NEVER)
				.vbarPolicy(ScrollBarPolicy.AS_NEEDED)
				.styleClass("question-scroll-pane")
				.build();

		final VBox vBox = VBoxBuilder.create()
				.alignment(Pos.CENTER)
				.spacing(10.0)
				.style("fx-background-color: rgb(255,255,255,0.5);")
				.build();

		
		//Is het een vraag of een 
		int size = questions == null ? 0 : questions.size();

		if(param == size + 1) {
			//is het de laatste ronde??
			if(roundNumber >= rounds.size() - 1) {
				getEndPane(vBox);
			}
		} else if(param == 0) { //de rondenaam weergeven
			getRoundPane(vBox);
		} else { //de vraag weergeven
			diplayQuestion(vBox,param - 1);
		}


		//zorgen dat de box aan de breedte van zijn vader vast blijft hangen
		vBox.minWidthProperty().bind(sPane.widthProperty().add(-10));
		vBox.maxWidthProperty().bind(sPane.widthProperty());
		vBox.minHeightProperty().bind(sPane.heightProperty().add(-20));

		//door de uitvoering te vertragen wordt er wel een scrollbar
		//toegevoegd zonder te moeten schalen van het venster. Geen idee waarom dit nodig is
		//maar het werkt wel.
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				sPane.setContent(vBox);
			}
		});

		return sPane;	
	}

	private void diplayQuestion(VBox content, int i) {
		Question q = questions.get(i);
		this.currentQuestion.set(q);
		
		Text question = new Text(q.getQuestion());
		question.getStyleClass().add("question-text");
		question.wrappingWidthProperty().bind(content.widthProperty().add(-20));
		
		content.getChildren().add(question);
		
		
		//TODO: mss een spacing element toevoegen
		if(q instanceof MultipleChoise) {
			MultipleChoise m = (MultipleChoise) q;
			//mogelijkheden toevoegen
			for(String choise: m.getValues()) {
				Text pos = new Text(choise);
				pos.getStyleClass().add("multiple-choise-text");
				pos.wrappingWidthProperty().bind(content.widthProperty().add(-20));
				
				if(choise.equals(m.getCorrectAnswer())) {
					pos.getStyleClass().add("correct-answer");
				
				}
				
				content.getChildren().add(pos);
			}
			//het correcte antwoord moet niet toegevoegd worden
			return;
			
		} else if(q instanceof PictureQuestion) {
			PictureQuestion p = (PictureQuestion) q;
			
			try {
				ImageView image = new ImageView(p.getPicture().getImage());
				image.getStyleClass().add("question-image");
				content.getChildren().add(image);
			} catch (IOException | SQLException  e) {
				messageMaker.showError("kon afbeelding niet laden");
			} 
		}
		
		
		Text answer = new Text(q.getCorrectAnswer());
		answer.getStyleClass().add("answer-text");
		answer.wrappingWidthProperty().bind(content.widthProperty().add(-20));
		
		content.getChildren().add(answer);
		
		if(q instanceof VideoQuestion || q instanceof MusicQuestion) {
			
			//controls aanmaken
			try{
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(MediaControllerController.class.getResource("mediaController.fxml"));
				AnchorPane controller = (AnchorPane) loader.load();
				controller.getStyleClass().add("question-mediacontrols");
				content.getChildren().add(controller);
				
				MediaControllerController c = loader.getController();
				
				if(q instanceof VideoQuestion) {
					VideoQuestion v = (VideoQuestion) q;
					ScreenManeger.getInstance().CreateMediaFrame(c, v.getMediaResource(), true);
				} else {
					MusicQuestion m = (MusicQuestion) q;
					ScreenManeger.getInstance().CreateMediaFrame(c, m.getMediaResource(), false);
				}
				
				
			} catch(Exception e) {
				messageMaker.showError(e.getMessage());
			}
		}
	}

	private void getEndPane(VBox content) {
		
		Text t = new Text("Einde van de quiz");
		t.getStyleClass().add("finish-text");
		t.wrappingWidthProperty().bind(content.widthProperty().add(-20));

		content.getChildren().add(t);
	}

	private void getRoundPane(VBox content) {
		String name = this.currentRound.get() == null ? 
				"Quiz zonder ronden"  : this.currentRound.get().getName();
		
		Text t = new Text(name);
		t.getStyleClass().add("round-text");
		t.wrappingWidthProperty().bind(content.widthProperty().add(-20));

		content.getChildren().add(t);
	}

	
	protected void togleSlideIn() {
		slideInAnimations.stop();

		if(slidedOut) {
			rotateAnimation.setToAngle(0.0);
			translateAnimation.setToX(-this.slideInPane.getPrefWidth());
			slidedOut = false;
			questionNavigator.requestFocus();
		} else {
			rotateAnimation.setToAngle(180.0);
			translateAnimation.setToX(0.0);
			slidedOut = true;
			roundsList.getSelectionModel().select(currentRound.get());
			roundsList.requestFocus();
		}

		slideInAnimations.play();

	}

	protected void playHide() {
		fadeAnimation.stop();
		fadeAnimation.setDelay(Duration.millis(500));
		fadeAnimation.setToValue(0.0);
		fadeAnimation.play();

		//delay terug verwijderen voor volgende animatie
		fadeAnimation.setDelay(Duration.millis(0));
	}

	protected void playShow() {
		fadeAnimation.stop();
		fadeAnimation.setToValue(MAX_SLIDEBAR_OPACITY);
		fadeAnimation.play();

	}

	private void setQuestionRound(QuestionRound r) {
		if(r == currentRound.get()) {
			//de tezetten ronde is de huidige ronde
			return;
		}
		currentRound.set(r);
		questions = r.getQuestions();
		//zorgen dat er geen question huide question is
		currentQuestion.set(null);
		roundNumber = rounds.indexOf(r);
		
		//de volgend ronde laden
		questionNavigator.setPageCount(questions.size() + 2);
		if(questionNavigator.getCurrentPageIndex() == 0) {
			//trukje om de eerste pagina toch weer te geven
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					questionNavigator.setCurrentPageIndex(0);
				}
			});
		} else {
			questionNavigator.setCurrentPageIndex(0);
		}
	}

	//properties waar andere objecten heen kunnen luisteren zoals de mediaPane
	public ReadOnlyObjectProperty<Question> getCurrentQuestionProperty() {
		return currentQuestion;
	}

	public ReadOnlyObjectProperty<QuestionRound> getCurrentRoundProperty() {
		return currentRound;
	}

	public ReadOnlyObjectProperty<Resource> getCurrentResourceProperty() {
		return currentResource;
	}

	//properties waar andere objecten heen kunnen luisteren zoals de mediaPane
	public Question getCurrentQuestion() {
		return currentQuestion.get();
	}

	public QuestionRound getCurrentRound() {
		return currentRound.get();
	}

	public Resource getCurrentResource() {
		return currentResource.get();
	}

}
