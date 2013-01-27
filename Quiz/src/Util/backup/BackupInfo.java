/**
 * Info van een backupFile
 * @author vrolijkx
 */
package Util.backup;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * get the information of a backupFile.
 * Use getBackupInfo(File backupFile) instead of constructor.
 * @author vrolijkx
 *
 */
public class BackupInfo {
	private Date backupDate;
	private int amountOfFiles;
	private String databaseUser;
	private String databaseName;
	private File databaseLocation;
	private File path;
	
	private BackupInfo() {} //de publieke constructor ongebruikbaar maken
	
 	public Date getBackupDate() {
		return backupDate;
	}
	
	public int getAmountOfFiles() {
		return amountOfFiles;
	}
	
	public String getDatabaseUser() {
		return databaseUser;
	}
	
	public String getDatabaseName() {
		return databaseName;
	}
	
	public File getDatabaseLocation() {
		return databaseLocation;
	}
	
	public File getPath() {
		return path;
	}
	
	@Override
	public String toString() {
		return String.format("backupDate : %s\n" +
				"previous database location: %s\n" +
				"database name: %s\n" +
				"database user: %s\n" +
				"backup location: %s\n" +
				"consist of %d files\n", 
				backupDate,databaseLocation,databaseName,databaseUser,path,amountOfFiles);
	}
	
 	private boolean parseInfo(String info) {
		String[] split;
		String propertie;
		String value;
		
		if(info == null) {
			return false;
		}
		 
		split = info.split(":");
		if(split.length != 2) {
			return false;
		}
		
		propertie = split[0].trim().toLowerCase();
		value = split[1].trim().toLowerCase();
		
		if(propertie.contains("user")) {
			this.databaseUser = value;
		} else if(propertie.contains("database name")) {
			this.databaseName = value;
		} else if(propertie.contains("database location")) {
			this.databaseLocation = new File(value);
		} else if(propertie.contains("backupdate")) {
			try {
				long sec = Long.parseLong(value);
				this.backupDate = new Date(sec);
			} catch(NumberFormatException e) {
				return false;
			}
			
		} else if(propertie.contains("files")) {
			try {
				this.amountOfFiles = Integer.parseInt(value);
			} catch(NumberFormatException e) {
				return false;
			}
		} else {
			return false;
		}
		return true;		
	}
	
	@SuppressWarnings("resource")
	public static BackupInfo getBackupInfo(File backupFile) throws IOException {
		BackupInfo info = new BackupInfo();
		InputStream input = null;
		ZipInputStream zipInput = null;
		ZipEntry entry = null;
		Scanner scanner = null;
		
		try {
			input = new FileInputStream(backupFile);
			zipInput = new ZipInputStream(input);
			
			while(true) {
				entry = zipInput.getNextEntry();
				if(entry == null) {
					throw new IOException("Could not find backup-info Entry");
				} else if(entry.getName().equals("backup-info")) {
					scanner = new Scanner(zipInput);
					String read;
					while(scanner.hasNextLine()) {
						read = scanner.nextLine();
						if(!info.parseInfo(read)) {
							System.out.println("Could not parse: " + read);
						}
					}
					info.path = backupFile;
					
					return info;
				}
			}	
			
		} catch(IOException e) {
			throw new IOException("Could't create backup info",e);
		} finally {
			if(scanner != null) {
				scanner.close();
			}
		}
	}
	
}
