package Util.backup;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class BackupTest {
	//Test main
	public static void main(String[] args) {
		try {
			Backup.BackupTo(new File("/Users/vrolijkx/Desktop/"),"backup2",new BackupProgressAdapter());
			if(Backup.isValidBackup(new File("/Users/vrolijkx/Desktop/backup.quiz"))) {
				System.out.println("backup terug gevonden");
			}
			Backup.RestorBackup(new File("/Users/vrolijkx/Desktop/backup2.quiz"));
			BackupInfo b = BackupInfo.getBackupInfo(new File("/Users/vrolijkx/Desktop/backup.quiz"));
			System.out.println("\n\n" + b);
			
			
			System.out.println("\n");
			ArrayList<BackupInfo> list = BackupInfo.searchBackups(new File("/"), true, true);
			for(BackupInfo info: list) {
				System.out.println(info + "\n");
			}
			System.out.println("done");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
