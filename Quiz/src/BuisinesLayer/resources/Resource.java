package BuisinesLayer.resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public abstract class Resource {
	protected File file;

	public Resource() {
	}

	public abstract File getFile() throws IOException;

	public InputStream getInputStream() throws IOException {
		return new FileInputStream(getFile());
	}
}