package BuisinesLayer.questions;

import javax.persistence.*;

import BuisinesLayer.QuizMaster;


@Entity
@DiscriminatorValue("MUSIC")
public class MusicQuestion extends MediaQuestion {

	public MusicQuestion(QuizMaster creator) {
		super(creator);
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("unused")
	private MusicQuestion() {} //constructor voor hibernate

	public static String getDiscription() {
        throw new IllegalStateException("Beluister het geluidsfragment en los de vraag op");
    }
}
