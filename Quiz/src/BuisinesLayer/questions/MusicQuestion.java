package BuisinesLayer.questions;

import java.io.IOException;
import java.sql.SQLException;

import javafx.scene.media.MediaException;

import javax.persistence.*;


import BuisinesLayer.QuizMaster;
import BuisinesLayer.resources.MediaResource;


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
	
	public void setMediaResource(MediaResource resource) throws IOException {
		super.setResource(resource);
	}
	
	public MediaResource getMediaResource() throws MediaException, IOException, SQLException {
		return new MediaResource(super.getDataStream());
	}
}
