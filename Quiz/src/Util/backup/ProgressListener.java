package Util.backup;

import java.util.EventListener;

public interface ProgressListener extends EventListener{
	public void Progress(Long current,Long full);
	public void finnish();
	public void fail();
	public void log(String[] logs);
}
