package installer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
/**
 * @author Roel
 */
public class Installer {
	public static void main(String arg[]) throws IOException
	{
		String regex = "<property name=\"hibernate.connection.url\">jdbc:h2:.*</property>";
		String replacement = "<property name=\"hibernate.connection.url\">jdbc:h2:%s</property>";
		String home = System.getProperty("user.home"); 
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File(home));
		String[] filetypes = {"zip", "jar"};
		
		fc.setFileFilter(new FileNameExtensionFilter("Gezipte bestanden",filetypes));
		fc.showOpenDialog(null);		
		File zipfile = fc.getSelectedFile();
		
		fc.setSelectedFile(new File("Quiz.jar"));
		fc.showSaveDialog(null);
		File destination = fc.getSelectedFile();
		
		fc.setFileFilter(new FileNameExtensionFilter("Database", "db"));
		fc.setSelectedFile(new File("Quiz"));
		fc.showSaveDialog(null);
		File db = fc.getSelectedFile();
		
		replacement = String.format(replacement, db.getAbsolutePath());
		
		File content = ZipUtil.getFromZip(zipfile, "hibernate.cfg.xml");
		File newcontent = new File("hibernate.cfg.xml");
		Scanner s = new Scanner(new FileInputStream(content));
		FileWriter out = new FileWriter(newcontent);
		while(s.hasNext()) {
			out.write(s.nextLine().replaceAll(regex, replacement));
		}
		s.close();
		out.close();
		
		ZipUtil.updateZip(zipfile, destination, newcontent);
		newcontent.delete();
	}
}
