package BussinesLayer.questions;

import java.io.IOException;
import java.sql.SQLException;

import javafx.scene.media.MediaException;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import BussinesLayer.QuizMaster;
import BussinesLayer.resources.MediaResource;


@Entity
@DiscriminatorValue("VIDEO")
public class VideoQuestion extends MediaQuestion{
	@Transient
	private MediaResource resource;
	
	public VideoQuestion(QuizMaster creator) {
		super(creator);
	}
	
	@SuppressWarnings("unused")
	private VideoQuestion() {} //constructor voor hibernate

	
	public void setMediaResource(MediaResource resource) throws IOException {
		this.resource = resource;
		super.setResource(resource);
	}
	
	public MediaResource getMediaResource() throws MediaException, IOException, SQLException {
		if(resource==null) {
			this.resource = new MediaResource(super.getDataStream());
		}
		
		return this.resource;
	}
}
