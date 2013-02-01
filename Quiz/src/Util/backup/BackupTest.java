package Util.backup;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class BackupTest {
	//Test main
	public static void main(String[] args) {
		File f;
		if(args.length == 1) {
			f = new File(args[0]);
		} else {
			System.out.println("geef de backuplocatie mee");
			return;
		}
		
		ArrayList<BackupInfo> b = BackupInfo.searchBackups(new File("/Users/vrolijkx"), true, true);
		for(BackupInfo be:b) {
			System.out.println(be);
			System.out.println();
		}
		
		if(f.isDirectory() && f.exists()) {
			try {
				System.out.println("Start Backup");
				Backup.BackupTo(f, "backup");
				System.out.println("backup finished");
			} catch (IOException e) {
				System.out.println("Backup failed");
			}
		} else {
			System.out.printf("%s is geen directory\n",f);
			return;
		}
	}
}
