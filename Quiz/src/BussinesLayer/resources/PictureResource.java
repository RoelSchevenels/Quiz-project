/**
 * Een resource voor een afbeelding
 * Uit de datebase te krijgen
 * @author vrolijkx
 */
package BussinesLayer.resources;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.hibernate.internal.util.BytesHelper;


import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class PictureResource extends Resource {
	private File file;
	private BufferedImage image;
	
	public PictureResource(BufferedImage image) {
		this.image = image;
	}
	
	public PictureResource(Image image) {
		this.image = SwingFXUtils.fromFXImage(image, null);
	}
	
	public PictureResource(File f) throws IOException {
		image = ImageIO.read(f);
		this.file = f;
	}
	
	public PictureResource(InputStream i) throws IOException {
		image = ImageIO.read(i);
	}
	
	public PictureResource(byte[] imageBytes) throws IOException {
		ByteArrayInputStream in = new ByteArrayInputStream(imageBytes);
		image = ImageIO.read(in);
	}
	
	/**
	 * @return the original image file or a temporary created file containing the image
	 * @throws IOException
	 */
	@Override
	public File getFile() throws IOException {
		if(file != null) {
			return this.file;
		}
		
		File temp = File.createTempFile("image_", ".jpg");
		temp.deleteOnExit();
		ImageIO.write( image, "jpg", temp );
		this.file = temp;
		return this.file;
	}
	
	public BufferedImage getBufferdImage() {
		return image;
	}
	
	public Image getImage() {
		return SwingFXUtils.toFXImage(image, null);
	}

	public byte[] getAsByteArray() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write( image, "jpg", baos );
		baos.flush();
		byte[] imageInBytes = baos.toByteArray();
		baos.close();
		
		return imageInBytes;
	}
}
