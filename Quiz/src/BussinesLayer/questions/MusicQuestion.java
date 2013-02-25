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
@DiscriminatorValue("MUSIC")
public class MusicQuestion extends MediaQuestion {
	@Transient
	private MediaResource resource;

	public MusicQuestion(QuizMaster creator) {
		super(creator);
	}
	
	@SuppressWarnings("unused")
	private MusicQuestion() {} //constructor voor hibernate

	public static String getDiscription() {
        throw new IllegalStateException("Beluister het geluidsfragment en los de vraag op");
    }
	
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
