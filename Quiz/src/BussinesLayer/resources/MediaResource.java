/**
 * een simpele resource voor de mediaspeler
 * die in en uit de database gelezen kan worden
 * @author vrolijkx
 */
package BussinesLayer.resources;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


import Util.IO;

import javafx.scene.media.Media;
import javafx.scene.media.MediaException;

/**
 * bezit een verwijzing naar
 * @author vrolijkx
 */

public class MediaResource extends Resource {
	private Media media;
	/**
	 * het creeren van media aan de hand van
	 * een inputStream kan tijdRoven zijn bij grote bestanden
	 * @param f
	 */
	public MediaResource(InputStream s) throws IOException, MediaException {
		file = File.createTempFile("media_", "med");
		file.deleteOnExit();
		OutputStream o = new FileOutputStream(file);
		IO.InputToOutputStream(s, o);
		media = new Media(file.toURI().toString());
	}
		
	public MediaResource(File f) throws MediaException, IOException{
		if(f.exists() && f.isFile()) {
			file = f;
			media = new Media(file.toURI().toString());
		} else {
			throw new IOException("Not a file");
		}
	}
	
	public Media getMedia() {
		return media;
	}

	@Override
	public File getFile() throws IOException {
		return this.file;
	}

	public long getSize() {
		return this.file.length();
	}

}
