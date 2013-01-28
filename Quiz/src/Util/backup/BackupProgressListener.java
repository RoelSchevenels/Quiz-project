/**
 * een progresslistener voor de vorderingen in
 * de backup te kunnen volgen.
 * @author vrolijkx
 */
package Util.backup;

import java.util.EventListener;

public interface BackupProgressListener extends EventListener{
	public void start(Long maxSize);
	/**
	 * @param the size to add to the already known size
	 */
	public void progress(int size); 
	public void logg(String s);
	public void fail();
	public void finish();
	
}
