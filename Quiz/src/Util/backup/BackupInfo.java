/**
 * Info van een backupFile
 * @author vrolijkx
 */
package Util.backup;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import Util.backup.Backup;

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
	
	/**
	 * probeert de backupinfo van een file te vinden.
	 * @param f
	 * @throws IOException
	 */
	public BackupInfo(File f) throws IOException {
		create(f);
	}
	
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
				"consist of %d files", 
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
	
 	private void create(File f) throws IOException {
 		InputStream input = null;
		ZipInputStream zipInput = null;
		ZipEntry entry = null;
		Scanner scanner = null;
		
		try {
			input = new FileInputStream(f);
			zipInput = new ZipInputStream(input);
			
			while(true) {
				entry = zipInput.getNextEntry();
				if(entry == null) {
					throw new IOException("Could not find backup-info Entry");
				} else if(entry.getName().toLowerCase().equals("backup-info")) {
					scanner = new Scanner(zipInput);
					String read;
					while(scanner.hasNextLine()) {
						read = scanner.nextLine();
						if(!this.parseInfo(read)) {
							System.out.println("Could not parse: " + read);
						}
					}
					this.path = f;
					break;
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
 	
	
	/**
	 * Doorzoekt de directory op backup files
	 * 
	 * @param dir directory where you want to search
	 * @param recursifly true if you want to search the folder recursifly
	 * @param exhaustive true if you want to check every file with every extenstion
	 * @return list with backupInfo files of all found vallid backup files.
	 */
	public static ArrayList<BackupInfo> searchBackups(File dir,boolean recursifly, boolean exhaustive) {
		ArrayList<BackupInfo> backupInfo = new ArrayList<BackupInfo>();
		searchBackups(backupInfo, dir, recursifly, exhaustive);
		return backupInfo;
	}
	
	private static void searchBackups(ArrayList<BackupInfo> info,File file,boolean recursifly, boolean exhaustive) {
		if(!file.exists() || !file.canRead()) {
			return;
		} else if(file.isDirectory() && recursifly) {
			for (File child : file.listFiles()) {
				searchBackups(info,child, recursifly, exhaustive);
			}
			
		} else if(file.isFile()) {
			if(exhaustive && !file.getName().toLowerCase().contains(".quiz")) {
				return;
			}
			if(Backup.isValidBackup(file)) {
				try {
					info.add(new BackupInfo(file));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} 
	}
	
}
