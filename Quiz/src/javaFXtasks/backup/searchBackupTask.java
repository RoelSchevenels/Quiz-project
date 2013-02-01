/**
 * een task voor het zoeken naar backups
 * 
 */
package javaFXtasks.backup;

import Util.backup.BackupInfo;
import java.io.File;
import java.util.ArrayList;
import javafx.concurrent.Task;

/**
 *
 * @author vrolijkx
 */
public class searchBackupTask extends Task<ArrayList<BackupInfo>> {
    private final File file;
    private ArrayList<BackupInfo> b;
    
    public searchBackupTask(File f) {
        this.file = f;
    }
    
    public ArrayList<BackupInfo> getBackupInfo() {
        return b;
    }
    
    @Override
    protected ArrayList<BackupInfo> call() throws Exception {
        b = BackupInfo.searchBackups(file, true, true);
        return b; 
    }
  
}
