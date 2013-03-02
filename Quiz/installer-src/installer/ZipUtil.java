package installer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
/**
 * @author Roel
 */
public class ZipUtil {
	public static File getFromZip(File file, String filename) throws IOException
	{
		File tmp = File.createTempFile(file.getName(), filename);
		ZipInputStream zin = new ZipInputStream(new FileInputStream(file));
		FileOutputStream out = new FileOutputStream(tmp);
		byte[] buf = new byte[1024 * 1024];	//Buffer maximum megabyte groot
		int len;
		
		for(ZipEntry e=zin.getNextEntry();zin.available()>0;e=zin.getNextEntry()) {
			if(e.getName().equals(filename)){
				System.out.println(filename);
				break;
			}
		}
		while((len = zin.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		zin.close();
		out.close();
		
		return tmp;
	}
	
	public static void updateZip(File inputZip, File outputZip, File file) throws IOException
	{
		File[] files = {file};
		updateZip(inputZip, outputZip,files);
	}
	
	public static void updateZip(File inputZip, File outputZip, File[] files) throws IOException
	{
		byte[] buf = new byte[1024 * 1024];	//Megabyte

		ZipInputStream zin = new ZipInputStream(new FileInputStream(inputZip));
		ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(outputZip));

		ZipEntry entry = zin.getNextEntry();
		while (entry != null) {
			String name = entry.getName();
			boolean notInFiles = true;
			for (File f : files) {
				if (f.getName().equals(name)) {
					notInFiles = false;
					break;
				}
			}
			if (notInFiles) {
				// Add ZIP entry to output stream.
				zout.putNextEntry(new ZipEntry(name));
				// Transfer bytes from the ZIP file to the output file
				int len;
				while ((len = zin.read(buf)) > 0) {
					zout.write(buf, 0, len);
				}
			}
			entry = zin.getNextEntry();
		}
		// Close the streams
		zin.close();
		// Compress the files
		for (int i = 0; i < files.length; i++) {
			InputStream in = new FileInputStream(files[i]);
			// Add ZIP entry to output stream.
			zout.putNextEntry(new ZipEntry(files[i].getName()));
			// Transfer bytes from the file to the ZIP file
			int len;
			while ((len = in.read(buf)) > 0) {
				zout.write(buf, 0, len);
			}
			// Complete the entry
			zout.closeEntry();
			in.close();
		}
		// Complete the ZIP file
		zout.close();
	}

}
