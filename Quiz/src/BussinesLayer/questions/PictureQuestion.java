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
		this.setImage(p.getBufferdImage());
	}
	
	private void setImage(BufferedImage image) throws IOException {
		//image naar een byteArray omzetten
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write( image, "jpg", baos );
		baos.flush();
		byte[] imageInBytes = baos.toByteArray();
		baos.close();
		//bytes naar de blob omzetten
		System.out.println(imageInBytes.length);
		this.setData(imageInBytes);
		
	}
}
