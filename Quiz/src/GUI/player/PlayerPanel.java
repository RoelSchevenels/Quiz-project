package GUI.player;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public class PlayerPanel extends JPanel
{
	private QuestionPanel pnlQuestion;
	private AnswerPanel pnlAnswer;
	
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
	}
	
	public void ChangeQuestion(String question, int questionType, boolean vocalAnswer)
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
			add(pnlQuestion);
			
			break;
		}
		
		//Set Answer panel
		if(vocalAnswer)
		{
			//SpeedAnswerPanel
		}
		else if(questionType == MULTIPLE_CHOICE_QUESTION)
		{
			//MultipleChoiceAnswerPanel
		}
		else
		{
			//OpenAnswerPanel
			pnlAnswer = new OpenAnswerPanel();
			add(pnlAnswer, BorderLayout.CENTER);
		}
		
		revalidate();
	}
}
