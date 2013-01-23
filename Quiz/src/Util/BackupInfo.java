/**
 * Info van een backupFile
 * @author vrolijkx
 */
package Util;

import java.io.File;
import java.util.Date;

public class BackupInfo {
	private Date backupDate;
	private int amountOfFiles;
	private String databaseUser;
	private String databaseName;
	private File databaseLocation;
	
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
}
