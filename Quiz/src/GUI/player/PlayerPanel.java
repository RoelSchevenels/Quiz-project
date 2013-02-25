/**
 * @author Jorne De Smedt
 */
package GUI.player;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javassist.expr.Instanceof;

import javax.swing.JPanel;

import BussinesLayer.questions.*;

public class PlayerPanel extends JPanel
{
	private QuestionPanel pnlQuestion;
	private AnswerPanel pnlAnswer;
	
	private Question question;
	
	public void setQuestion(Question question)
	{
		this.question =  question;
	}
	
	//Question Types
	public static final int KNOWLEDGE_QUESTION = 0;
	public static final int FOTO_QUESTION = 1;
	public static final int VIDEO_QUESTION = 2;
	public static final int MUSIC_QUESTION = 3;
	public static final int MULTIPLE_CHOICE_QUESTION = 4;
	
	
	public PlayerPanel()
	{
		setLayout(new BorderLayout());
		
		pnlQuestion = new QuestionPanel("What is a question?");
		add(pnlQuestion, BorderLayout.NORTH);
		
		pnlAnswer = new AnswerPanel();
		add(pnlAnswer, BorderLayout.CENTER);
		
		addPropertyChangeListener(new QuestionListener());
	}
	
	public void changeQuestion(boolean vocalAnswer) //Newer version using the Question class
	{
		remove(pnlQuestion);
		remove(pnlAnswer);
		
		if(question instanceof StandardQuestion)
		{
			//TODO: Standard Question
			pnlQuestion = new QuestionPanel(question.getQuestion());
			add(pnlQuestion, BorderLayout.NORTH);
		}
		else if(question instanceof MultipleChoise)
		{
			//TODO: Multiple choice Question
			pnlQuestion = new QuestionPanel(question.getQuestion());
			add(pnlQuestion, BorderLayout.NORTH);
			
			ArrayList<String> answers = new ArrayList<String>();
			Collections.addAll(answers, ((MultipleChoise) question).getValues());
			
			pnlAnswer = new MultipleChoiceAnswerPanel(answers);
			
			((MultipleChoiceAnswerPanel) pnlAnswer).drawButtons(MultipleChoiceAnswerPanel.DOUBLE_ROW);
		}
		else if(question instanceof PictureQuestion)
		{
			//TODO: Picture choice Question			
		}
		else if(question instanceof MusicQuestion)
		{
			//TODO: Music choice Question			
		}
		else if(question instanceof VideoQuestion)
		{
			//TODO: Video choice Question			
		}
		else
		{
			System.out.println("Not a valid question type : " + question.getClass().getSimpleName());
		}
		
		if(vocalAnswer)
		{
			//SpeedAnswerPanel
			pnlAnswer = new SpeedAnswerPanel();
		}
		else if(!(question instanceof MultipleChoise))
		{
			pnlAnswer = new OpenAnswerPanel();
		}
		
		add(pnlAnswer, BorderLayout.CENTER);
		
		revalidate();
	}
	
	public void ChangeQuestion(String question, int questionType, boolean vocalAnswer) //outdated
	{
		remove(pnlQuestion);
		remove(pnlAnswer);
		
		//Set Question Panel
		switch (questionType)
		{
		case FOTO_QUESTION:
			
			break;
		case VIDEO_QUESTION:
			
			break;
		case MUSIC_QUESTION:
		
			break;
		case MULTIPLE_CHOICE_QUESTION:
			
			break;

		default:
			//Knowledge Question
			pnlQuestion = new QuestionPanel(question);
			add(pnlQuestion, BorderLayout.NORTH);
			
			break;
		}
		
		//Set Answer panel
		if(vocalAnswer)
		{
			//SpeedAnswerPanel
			pnlAnswer = new SpeedAnswerPanel();
		}
		else if(questionType == MULTIPLE_CHOICE_QUESTION)
		{
			//MultipleChoiceAnswerPanel
		}
		else
		{
			//OpenAnswerPanel
			pnlAnswer = new OpenAnswerPanel();			
		}
		add(pnlAnswer, BorderLayout.CENTER);
		
		revalidate();
	}
	
	private class QuestionListener implements PropertyChangeListener
	{
		@Override
		public void propertyChange(PropertyChangeEvent evt)
		{
			// TODO Auto-generated method stub
			
			if(evt.getPropertyName().equals("question"))
			{
				System.out.println("Question Changed");
			}
		}		
	}
}
