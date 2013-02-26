/**
 * een foto vraag
 */
package BussinesLayer.questions;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import BussinesLayer.QuizMaster;
import BussinesLayer.resources.PictureResource;

@Entity
@DiscriminatorValue("PICTURE")
public class PictureQuestion extends MediaQuestion{
	
	public PictureQuestion(QuizMaster creator) {
		super(creator);
	}
	
	@SuppressWarnings("unused")
	private PictureQuestion() {} //hibernate constructor
	
	public PictureResource getPicture() throws IOException, SQLException {
		return new PictureResource(getDataStream());
	}
	
	public void setPicture(PictureResource p) throws IOException {
		setData(p.getAsByteArray());
	}
	
}
