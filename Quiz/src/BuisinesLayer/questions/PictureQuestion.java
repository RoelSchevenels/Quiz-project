/**
 * een foto vraag
 */
package BuisinesLayer.questions;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.persistence.*;

import BuisinesLayer.QuizMaster;

@Entity
@DiscriminatorValue("PICTURE")
public class PictureQuestion extends MediaQuestion{
	
	public PictureQuestion(QuizMaster creator) {
		super(creator);
	}
	
	private PictureQuestion() {} //hibernate constructor
	
	public Image getImage() throws IOException, SQLException {
		return ImageIO.read(getDataStream());
	}
	
	public void setImage(BufferedImage image) throws IOException {
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
