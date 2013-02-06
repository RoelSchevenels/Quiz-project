package BuisinesLayer.questions;

import java.io.IOException;
import java.sql.SQLException;

import javafx.scene.media.MediaException;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import BuisinesLayer.QuizMaster;
import BuisinesLayer.resources.MediaResource;


@Entity
@DiscriminatorValue("VIDEO")
public class VideoQuestion extends MediaQuestion{
	
	public VideoQuestion(QuizMaster creator) {
		super(creator);
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("unused")
	private VideoQuestion() {} //constructor voor hibernate

	
	public void setMediaResource(MediaResource resource) throws IOException {
		super.setResource(resource);
	}
	
	public MediaResource getMediaResource() throws MediaException, IOException, SQLException {
		return new MediaResource(super.getDataStream());
	}
}
