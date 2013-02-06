/**
 * kleine io utils
 * @author vrolijkx
 */
package Util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IO {

	/**
	 * kopieert de inputstream naar de outputstream.
	 * en sluit beide streams.
	 * @param input
	 * @param output
	 * @throws IOException wanneer het fout gaat
	 */
	public static void InputToOutputStream(InputStream input,OutputStream output) throws IOException{
		byte[] buffer = new byte[1024];
		int flushCounter = 0;
		int read;

		try{
			//blokken lezen
			while(-1 != (read = input.read(buffer))) {
				flushCounter += read;
				output.write(buffer, 0, read);

				//na 1mb output flushen
				if(flushCounter >= 1048576) {
					flushCounter = 0;
					output.flush();
				}
			}
			
		} catch (IOException e) {
			throw new IOException("Could not complet copie",e);
		} finally {
			try {
				if(input!=null) {
					input.close();
				}
				if(output!=null) {
					output.close();
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
}

