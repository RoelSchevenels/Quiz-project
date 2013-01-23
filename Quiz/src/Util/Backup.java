/**
 * Een mooie hulp klassen voor het backuppen van de database
 * En het restoren van de backup.
 * @author vrolijkx
 */
package Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Formatter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.hibernate.cfg.Configuration;

public class Backup {
	public static void BackupTo(File location,String name) throws IOException {
		File outputFile;
		Configuration c = ConnectionUtil.getHibernateConfiguration();
		name += ".quiz";
		
		try {
			File databasLocation = getDatabaseLocation(c);
			System.out.println(databasLocation);
			
			if(databasLocation.isDirectory() && location.isDirectory()) {
				outputFile = new File(location,name);
				outputFile.createNewFile();
				zipFolder(databasLocation, outputFile, c);
				
				
			} else {
				throw new IOException("Location is not valid");
			}
			
		} catch (IOException e) {
			throw new IOException("Zip failed");
		}
	}
		
	private static void zipFolder(File toZipFolder, File outputFile, Configuration config) throws IOException {
		Formatter formatter = null;
		ZipEntry entry;
		OutputStream output = null;
		ZipOutputStream zipOutput = null;
		InputStream fileInput = null;
		byte[] buffer = new byte[1024]; // 1kb buffer
		File files[];		
		
		//de te zippen files
		files = toZipFolder.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.contains(".db");
			}
		});
		
		
		try {
			System.out.println("Backup Started");
			output = new FileOutputStream(outputFile);
			zipOutput = new ZipOutputStream(output);
			fileInput = null;
			
			//info van de backup toevoegen
			entry = new ZipEntry("backup-info");
			entry.setComment("Extra backup info");
			zipOutput.putNextEntry(entry);
			formatter = new Formatter(zipOutput);
			formatter.format("BackupDate: %d\n",new Date().getTime());
			formatter.format("files: %d\n", files.length);
			formatter.format("Database User: %s\n", config.getProperty("hibernate.connection.username" ));
			formatter.format("Database name: %s\n", getDatabaseName(config));
			formatter.format("Database location: %s\n", getDatabaseLocation(config));
			formatter.flush();
			zipOutput.flush();
			zipOutput.closeEntry();
			//einde info
			
			//alle files zippen
			for(File fi : files) {
				System.out.println("toevoegen aan zip : " + fi.getName());
				entry = new ZipEntry(fi.getName());
				zipOutput.putNextEntry(entry);
				
				fileInput = new FileInputStream(fi);
				int read;
				int flushCounter = 0;
				//stream in blokken van 1kb
				while(-1 != (read = fileInput.read(buffer))) {
					flushCounter += read;
					zipOutput.write(buffer, 0, read);
					
					//na 1mb output flushen
					if(flushCounter >= 1048576) {
						zipOutput.flush();
						flushCounter = 0;
					}	
				}
				
				fileInput.close();
				zipOutput.flush();
				//zipOutput.closeEntry();
				
				
				//input sluiten
				System.out.println("toevoegevoegd aan zip : " + fi.getName());
			}
			System.out.println("zip ok");
			
		} finally { //alles sluiten
			if(formatter != null) {
				formatter.close();
			}
		}
	}
	
	public static File getDatabaseLocation(Configuration c) throws IOException {
		String place = c.getProperty("hibernate.connection.url").split(":")[2];
		File databasLocation = new File(place);
		databasLocation = new File(databasLocation.getCanonicalPath()).getParentFile();
		return databasLocation;
		
	}
	
	public static String getDatabaseName(Configuration c) throws IOException {
		String place = c.getProperty("hibernate.connection.url").split(":")[2];
		File databasLocation = new File(place);
		return databasLocation.getName();
	}
	
	public static void RestorBackup(File backupFile) throws IOException {
		//check the backup
		if(!isValidBackup(backupFile)) {
			throw new IOException("Invalid backupFile");
		}
		//variabelen declareren
		File databaseLocation;
		File outputFile;
		InputStream input = null;
		ZipInputStream zipInput = null;
		ZipEntry entry = null;
		OutputStream output = null;
		byte[] buffer = new byte[1024]; //buffer van een 1kb.
		long readedSize = 0;
		long totalSize = backupFile.length();
		Configuration c = ConnectionUtil.getHibernateConfiguration();
		
		try {
			databaseLocation = getDatabaseLocation(c);
			input = new FileInputStream(backupFile);
			zipInput = new ZipInputStream(input);

			
			while(true) {
				entry = zipInput.getNextEntry();
				if(entry==null) {
					return;
				}else if(entry.getName().equals("backup-info")) {
					//de size van de backup-info bij de gelezen bytes optellen
					readedSize += entry.getSize();
					// TODO: progressListener updaten;
				} else {
					System.out.println("Restoring: " + entry.getName());
					outputFile = new File(databaseLocation,entry.getName());
					outputFile.createNewFile();
					output = new FileOutputStream(outputFile);
					int flushCounter = 0;
					int read;
					
					//blokken lezen
					while(-1 != (read = zipInput.read(buffer))) {
						flushCounter += read;
						readedSize += read;
						output.write(buffer, 0, read);
						
						//na 1mb output flushen
						if(flushCounter >= 1048576) {
							flushCounter = 0;
							//progress listener updaten
							output.flush();
						}	
					}
					output.close();
					
					System.out.println("Restored: " + entry.getName());
				};
				
				System.out.println("Restore Complete");
			}	
		} catch(IOException e) {
			throw new IOException("Restore failed");
		} finally {
			if(zipInput != null) {
				try {
					zipInput.close();
				} catch (IOException e) {
					
				}
			}
		}
		
		
	}
	
	public static boolean isValidBackup(File backupFile) {
		InputStream input = null;
		ZipInputStream zipInput = null;
		ZipEntry entry = null;
		
		try {
			input = new FileInputStream(backupFile);
			zipInput = new ZipInputStream(input);
			
			do {
				entry = zipInput.getNextEntry();
				if(entry == null) {
					return false;
				} else if(entry.getName().equals("backup-info")) {
					return true;
				}
			}while(true);
			
			
		} catch(IOException e) {
			return false;
		} finally {
			if(zipInput != null) {
				try {
					zipInput.close();
				} catch (IOException e) {
					
				}
			}
		}
		
	}
	
	
	
	//Test main
	public static void main(String[] args) {
		try {
			BackupTo(new File("/Users/vrolijkx/Desktop/"),"backup");
			if(isValidBackup(new File("/Users/vrolijkx/Desktop/backup.quiz"))) {
				System.out.println("backup terug gevonden");
			}
			RestorBackup(new File("/Users/vrolijkx/Desktop/backup.quiz"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
