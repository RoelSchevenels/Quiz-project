/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javaFXpanels.CreateQuiz;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import BussinesLayer.QuizMaster;
import BussinesLayer.questions.MediaQuestion;
import BussinesLayer.questions.MultipleChoise;
import BussinesLayer.questions.MusicQuestion;
import BussinesLayer.questions.PictureQuestion;
import BussinesLayer.questions.Question;
import BussinesLayer.questions.StandardQuestion;
import BussinesLayer.questions.VideoQuestion;
import BussinesLayer.resources.MediaResource;
import BussinesLayer.resources.PictureResource;

/**
 * FXML Controller class
 *
 * @author Vrolijk Kristof <Vrolijkx.Kristof@gmail.com>
 */
public class CreateQuestionController implements Initializable {
	private FileChooser fileChooser;
	@FXML
	private AnchorPane root;
	@FXML
	private VBox multipleChoiseBox;
	@FXML
	private TextArea openQuestionText;
	@FXML
	private TextArea openAnswerText;
	@FXML
	private TextArea multipleQuestionText;
	@FXML
	private TextField multiplePosibilityText;
	@FXML
	private TextArea mediaQuestionText;
	@FXML
	private TextArea mediaAnswerText;
	@FXML
	private Label fileLabel;
	@FXML
	private Button openAcceptButton;
	@FXML
	private Button multipleAcceptButton;
	@FXML
	private Button mediaAcceptButton;
	@FXML
	private Button multipleAddButton;
	
	private EventHandler<ActionEvent> onCancle;
	private EventHandler<ActionEvent> onFinish;
	private Question currentQuestion;
	private ToggleGroup toggleGroup;
	private SimpleObjectProperty<File> currentFile;
	private QuizMaster creator;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	currentFile = new SimpleObjectProperty<File>();
    	fileChooser = new FileChooser();
    	fileChooser.getExtensionFilters().addAll(
    			new FileChooser.ExtensionFilter("media bestanden",
    					"*.jpg","*.png","*.img","*.mp4","*.flv","*.mp3"));
    	toggleGroup = new ToggleGroup();
        initBindings();
    }
    
    private void initBindings() {
    	//TODO bindings oplossen
    	BooleanBinding buttonVisible1 = openQuestionText.textProperty()
			.isNotEqualTo("")
			.and(openAnswerText.textProperty()
					.isNotEqualTo(""));
    	//TODO bindigs oplossen
    	buttonVisible1.addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(
						ObservableValue<? extends Boolean> observable,
						Boolean oldValue, Boolean newValue) {
					System.out.println("veranderd");
					if(newValue) {
						openAcceptButton.setDisable(false);
					} else {
						openAcceptButton.setDisable(true);
					}
				}
			});
		
		BooleanBinding buttonVisible2 = mediaQuestionText.textProperty()
			.isNotEqualTo("")
			.and(mediaAnswerText.textProperty()
				.isNotEqualTo(""))
			.and(fileLabel.textFillProperty().isNotEqualTo(""));
		
		buttonVisible2.addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(
					ObservableValue<? extends Boolean> observable,
					Boolean oldValue, Boolean newValue) {
					if(newValue) {
						mediaAcceptButton.setDisable(false);
					} else {
						mediaAcceptButton.setDisable(true);
					}
				}
			});
		
		//zorgt dat het file label up to date blijft
		fileLabel.textProperty().bind(
				Bindings.createStringBinding(new Callable<String>() {
					@Override
					public String call() throws Exception {
						if(currentFile.get() == null) {
							return "";
						} else {
							return currentFile.get().getName();
						}
					}
				}, currentFile));
	}

	@FXML
    private void cancel() {
    	if(onCancle != null) {
    		onCancle.handle(new ActionEvent());
    	}
    }
	
	private void finnish() {
		if(onFinish != null) {
			onFinish.handle(new ActionEvent());
		}
	}
	
	public void setOnCancel(EventHandler<ActionEvent> event) {
		onCancle = event;
	}
	
	public void setOnFinish(EventHandler<ActionEvent> event) {
		onFinish = event;
	}
	
	public void show(QuizMaster q) {
		//TODO: alles clearen
		creator = q;
		currentQuestion = null;
		currentFile.set(null);
		toggleGroup = new ToggleGroup();
		multipleChoiseBox.getChildren().clear();
		
	}
	
	public void show(Question q, QuizMaster creator) {
		show(creator);	
		currentQuestion = q;
		//TODO: de juise question velden invoeren
		
	}
	
    @FXML
    private void addMediaQuestion() {
    	//de er is geen huidige vraag of de huidige vraag is geen mediaVraag;
    	if(currentQuestion == null ||!(currentQuestion instanceof MediaQuestion)) {
    		if(currentFile.get() != null) {
    			String name = currentFile.get().getName();
    			if(name.endsWith("jpg") || name.endsWith("png") || name.endsWith("img")) {
    				PictureQuestion p = new PictureQuestion(creator);
    				PictureResource r;
					try {
						r = new PictureResource(currentFile.get());
						p.setPicture(r);
					} catch (IOException e) {
						return;
					}
    				currentQuestion = p;
    				
    				
    			} else if(name.endsWith(".mp3")) {
    				MusicQuestion m = new MusicQuestion(creator);
    				MediaResource r;
    				try {
    					r = new MediaResource(currentFile.get());
    					m.setMediaResource(r);
    				} catch(IOException e) {
    					return;
    				}
    				
    				currentQuestion = m;
    			} else {
    				VideoQuestion v = new VideoQuestion(creator);
    				MediaResource r;
    				try {
    					r = new MediaResource(currentFile.get());
    					v.setMediaResource(r);
    				} catch(IOException e) {
    					return;
    				}
    				currentQuestion = v;
    			}
    		} else {
    			//geen bestand geselcteed;
    			return;
    		}
    		
    		//TODO:opvangen als de media is verandert de question te veranderen
    		currentQuestion.setQuestion(mediaAnswerText.getText());
    		currentQuestion.setCorrectAnswer(mediaAnswerText.getText());
    		saveCurrentQuestion();
    	}
    }
    
    @FXML
    private void addOpenQuestion() {
    	if(currentQuestion == null || !(currentQuestion instanceof StandardQuestion)) {
			currentQuestion = new StandardQuestion(creator);
		}
    	currentQuestion.setCorrectAnswer(openAnswerText.getText());
    	currentQuestion.setQuestion(openQuestionText.getText());
    	
    	saveCurrentQuestion();
    }
    
    @FXML
    private void addMultipleChoiceQuestion() {
    	if(toggleGroup.getSelectedToggle() != null && toggleGroup.getToggles().size() >=2) {
    		if(currentQuestion == null || !(currentQuestion instanceof MultipleChoise)) {
    			currentQuestion = new MultipleChoise(creator);
    		}
    		currentQuestion.setQuestion(multipleQuestionText.getText());
    		//text van de geselecteerde radiobutton;
    		currentQuestion.setCorrectAnswer(((RadioButton)toggleGroup.getSelectedToggle()).getText());
    		
    		String[] possibible = new String[toggleGroup.getToggles().size()];
    		int i = 0;
    		for(Toggle b: toggleGroup.getToggles()) {
    			possibible[i] = ((RadioButton) b).getText();
    		}
    		((MultipleChoise) currentQuestion).setValues(possibible);
    		saveCurrentQuestion();
    		
    	}
    }
    
    private void saveCurrentQuestion() {
    	/**
    	Session s = ConnectionUtil.getSession();
    	Transaction t = s.beginTransaction();
    	s.saveOrUpdate(currentQuestion);
    	try {
    		t.commit();
    	} catch(HibernateException ex) {
    		ex.printStackTrace();
    	}
    	//s.close();
    	 */
    	finnish();
    }
    
    @FXML
    private void chooseFile() {
    	root.setMouseTransparent(true);
    	currentFile.set(fileChooser.showOpenDialog(null));
    	root.setMouseTransparent(false);
    }
    
    @FXML
    private void addpossibilty() {
    	if(!multiplePosibilityText.getText().isEmpty()) {
    		RadioButton b = new RadioButton(multiplePosibilityText.getText());
    		b.setToggleGroup(toggleGroup);
    		multipleChoiseBox.getChildren().add(b);
    	}
    }
    
    public Question getCurrentQuestion() {
    	return currentQuestion;
    }
}
