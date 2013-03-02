package installer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
/**
 * @author Roel
 */
public class Installer {
	private static JFileChooser jfc;
	public static void main(String arg[]) throws IOException
	{
		String[] regex = {"<property name=\"hibernate.connection.url\">jdbc:h2:.*</property>",
				"<property name=\"hibernate.show_sql\">.*</property>",
				" <property name=\"hibernate.use_sql_comments\">.*</property>"};
		String[] replacement = {"<property name=\"hibernate.connection.url\">jdbc:h2:%s</property>",
				"<property name=\"hibernate.show_sql\">false</property>",
		" <property name=\"hibernate.use_sql_comments\">false</property>"};
		String home = System.getProperty("user.home"); 
		jfc = new JFileChooser();
		jfc.setCurrentDirectory(new File(home));
		
		File zipfile = getFile("", false,"Gezipte bestanden", "jar","zip");		
		File destination = getFile("Quiz.jar",true,"Gezipte bestanden", "jar","zip");		
		File db = getFile("Quiz", true, "Database", "db");
		
		replacement[0] = String.format(replacement[0], db.getAbsolutePath());
		
		File content = ZipUtil.getFromZip(zipfile, "hibernate.cfg.xml");
		File newcontent = new File("hibernate.cfg.xml");
		Scanner s = new Scanner(new FileInputStream(content));
		FileWriter out = new FileWriter(newcontent);
		while(s.hasNext()) {
			String line = s.nextLine();
			for (int i=0;i<regex.length;i++) {
				line = line.replaceAll(regex[i], replacement[i]);
			}
			out.write(line);
		}
		s.close();
		out.close();
		
		ZipUtil.updateZip(zipfile, destination, newcontent);
		newcontent.delete();
	}
	
	private static File getFile(String initialName, boolean save, String filterName, String... filter)
	{
		jfc.setFileFilter(new FileNameExtensionFilter(filterName, filter));
		jfc.setSelectedFile(new File(initialName));
		int res;
		boolean exists = false;
		while(!exists) {
			if(save)
				res = jfc.showSaveDialog(null);
			else
				res = jfc.showOpenDialog(null);
			if(res == JFileChooser.APPROVE_OPTION) {
				File file = jfc.getSelectedFile();
				exists = file.exists();
				if(exists || save)
					return file;
			} else {
				JOptionPane.showMessageDialog(null, "Installatie geannuleerd");
				System.exit(0);
				return null;
			}
		}
		return null;
	}
}
