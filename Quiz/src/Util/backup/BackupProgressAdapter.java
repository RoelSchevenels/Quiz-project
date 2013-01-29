/**
 * A backupListener.
 * @author vrolijkx
 */
package Util.backup;

import java.util.ArrayList;

public class BackupProgressAdapter implements BackupProgressListener {
	private Long maxSize;
	private Long currentSize;
	private ArrayList<String> loggs;
	private boolean finnish;
	private boolean fail;
	
	public BackupProgressAdapter() {
		loggs = new ArrayList<String>();
		currentSize = 0L;
		finnish = false;
		
	}
	
	@Override
	public void start(Long maxSize) {
		this.maxSize = maxSize;
	}

	@Override
	public void progress(int size) {
		this.currentSize += size;
	}

	@Override
	public void logg(String s) {
		loggs.add(s);

	}

	@Override
	public void fail() {
		fail = true;
		finnish = true;

	}

	@Override
	public void finish() {
		finnish = true;

	}
	
	public Long getMaxSize() {
		return maxSize;
	}
	


}
