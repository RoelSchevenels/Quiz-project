/**
 * een task voor het uitvoeren van een backup
 * @author vrolijkx
 */
package javaFXtasks;

import java.io.File;

import javafx.concurrent.Task;
import Util.backup.Backup;
import Util.backup.BackupProgressAdapter;
import Util.backup.ProgressListener;

/**
 *
 * @author vrolijkx
 */
@SuppressWarnings("rawtypes")
public class BackupTask extends Task implements ProgressListener {
    private final File file;
    private final String name;
    private final boolean restore;
    
    
    /**
     * @param f the location where to backup to
     * @param name the name of the backup
     */
    public BackupTask(File f, String name) {
        this.file = f;
        this.name = name;
        this.restore = false;
    }
    
     /**
     * @param f the location where to restore from
     */
    public BackupTask(File f) {
        this.file = f;
        this.name = null;
        this.restore = true;
    }
    
    
    @Override
    protected Object call() throws Exception {
        BackupProgressAdapter adapter = new BackupProgressAdapter();
        adapter.addProgressListener(this);
        
        if(restore) {
            Backup.RestorBackup(file,adapter);
        } else {
            Backup.BackupTo(file, name, adapter);
        }
        //extra sleep om progress done tegoei weer te kunnen geven
        sleep();
        return null;
        
        
    }

    @Override
    public void Progress(Long current, Long full) {
        updateProgress(Math.min(current,full), full);
    }

    private void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    
    @Override
    public void finish() {
        updateProgress(1, 1);
        updateMessage("Finished");
    }

    @Override
    public void fail() {
        updateProgress(0, 1);
        updateMessage("Failed");
    }

    @Override
    public void log(String[] logs) {
        for(String s : logs) {
            updateMessage(s);
        }
    }


    
    
}
