/**
 * Een mooie hulp klassen voor het backuppen van de database
 * En het restoren van de backup.
 * @author vrolijkx
 */
package Util.backup;

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

import org.hibernate.Hibernate;
import org.hibernate.cfg.Configuration;

import Util.ConnectionUtil;

/**
 * verzameling statiche metodes voor backups
 * @author vrolijkx
 * @see ConnectionUtil is nodig voor locatie te bepalen
 * @see Hibernate
 */
public class Backup {
	public static void BackupTo(File location,String name) throws IOException {
		BackupTo(location, name,null);
	}
	
	public static void BackupTo(File location,String name, BackupProgressListener progress) throws IOException {
		File outputFile;
		Configuration c = ConnectionUtil.getHibernateConfiguration();
		ConnectionUtil.CloseSessionFactory();
		
		if(!name.endsWith(".quiz")) {
			name += ".quiz";
		}
			
		try {
			File databasLocation = getDatabaseLocation(c);
			if(databasLocation.isDirectory() && location.isDirectory()) {
				outputFile = new File(location,name);
				outputFile.createNewFile();
				zipFolder(databasLocation, outputFile,c ,progress);
				
				
			} else {
				throw new IOException("Location is not valid");
			}
			
		} catch (IOException e) {
			if(progress != null) {
				progress.fail();
			}
			throw new IOException("Zip failed",e);
		}
	}
		
	private static void zipFolder(File toZipFolder, File outputFile, Configuration config, BackupProgressListener progress) throws IOException {
		final boolean log = progress != null;
		long totalSize = 0L;
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
		
		for (File file : files) {
			totalSize += file.length();
		}
		
		try {
			if(log) {	
				progress.start(totalSize);
				progress.logg("Backup Started");
			}
			
			output = new FileOutputStream(outputFile);
			zipOutput = new ZipOutputStream(output);
			fileInput = null;
			
			//info van de backup toevoegen
			if(log) {	
				progress.logg("adding backup-info");
			}
			entry = new ZipEntry("backup-info");
			entry.setComment("Extra backup info");
			zipOutput.putNextEntry(entry);
			formatter = new Formatter(zipOutput);
			formatter.format("BackupDate: %d\n",new Date().getTime());
			formatter.format("files: %d\n", files.length);
			formatter.format("Database User: %s\n", config.getProperty("hibernate.connection.username" ));
			formatter.format("Database name: %s\n", getDatabaseName(config));
			formatter.format("Database location: %s\n", getDatabaseLocation(config));
			formatter.format("total size: %d\n", totalSize);
			formatter.flush();
			zipOutput.flush();
			zipOutput.closeEntry();
			//einde info
			
			//alle files zippen
			for(File fi : files) {
				if(log) {	
					progress.logg("adding to backup:" + fi.getName());
				}
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
						if(log) {
							progress.progress(flushCounter);
						}
						zipOutput.flush();
						flushCounter = 0;
					}	
				}
				
				if(log) {
					progress.progress(flushCounter);
				}
				
				fileInput.close();
				zipOutput.flush();
				
				if(log) {
					progress.logg("added to backup : " + fi.getName());
				}
			}
			
			if(log) {
				progress.logg("Backup Ok");
				progress.finish();
			}
			
		} finally {
			if(formatter != null) {
				formatter.close();
			}
		}
	}
	
	public static File getDatabaseLocation(Configuration c) throws IOException {
		String place = c.getProperty("hibernate.connection.url").split(":")[2];
		//tild teken enzo vervangen
		place = place.replace("~", System.getProperty("user.home"));
		
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
		RestorBackup(backupFile, null);
	}
	
	public static void RestorBackup(File backupFile,BackupProgressListener progress) throws IOException {
		final boolean log = progress != null;
		
		if(log) {
			progress.start(backupFile.length());
		}
		//check the backup
		if(!isValidBackup(backupFile)) {
			if(log) {
				progress.fail();
			}
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
		Configuration c = ConnectionUtil.getHibernateConfiguration();
		
		
		try {
			databaseLocation = getDatabaseLocation(c);
			input = new FileInputStream(backupFile);
			zipInput = new ZipInputStream(input);

			
			while(true) {
				entry = zipInput.getNextEntry();
				if(entry==null) {
					if(log) {
						progress.logg("Restore Complete");
						progress.finish();
					}
					return;
				}else if(entry.getName().equals("backup-info")) {
					//de size van de backup-info bij de gelezen bytes optellen
					if(log) {
						progress.progress((int) entry.getSize());
					}
				} else {
					if(log) {
						progress.logg("Restoring: " + entry.getName());
					}
					outputFile = new File(databaseLocation,entry.getName());
					outputFile.createNewFile();
					output = new FileOutputStream(outputFile);
					int flushCounter = 0;
					int read;
					
					//blokken lezen
					while(-1 != (read = zipInput.read(buffer))) {
						flushCounter += read;
						output.write(buffer, 0, read);
						
						//na 1mb output flushen
						if(flushCounter >= 1048576) {
							if(log) {
								progress.progress(flushCounter);
							}
							flushCounter = 0;
							output.flush();
						}	
					}
					output.close();
					if(log) {
						progress.progress(flushCounter);
					}
					if(log) {
						progress.logg("Restored: " + entry.getName());
					}
				}
				
			}
			
		} catch(IOException e) {
			if(log) {
				progress.fail();
			}
			throw new IOException("Restore failed",e);
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
	
}
