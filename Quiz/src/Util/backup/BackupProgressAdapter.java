/**
 * Een multithreaded BackupProgress Listener implementatie
 * zodat de backup niet te fel wordt onderbroken.
 * @author vrolijkx
 */
package Util.backup;

import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BackupProgressAdapter implements BackupProgressListener {
	private Vector<ProgressListener> listeners;
	private Long maxSize;
	private Long currentSize;
	private ArrayList<String> loggs;
	private boolean finnish;
	private boolean fail;
	private EventLoop loop;
	private ExecutorService ex;

	public BackupProgressAdapter() {
		listeners = new Vector<ProgressListener>();
		loggs = new ArrayList<String>();
		currentSize = 0L;
		finnish = false;
		fail = false;
		loop = new EventLoop();
		ex = Executors.newSingleThreadExecutor();
	}

	public void addProgressListener(ProgressListener l) {
		if(!listeners.contains(l)) {
			listeners.add(l);
		}
	}

	public void removeProgressListener(ProgressListener l) {
		if(listeners.contains(l)) {
			listeners.remove(l);
		}
	}

	@Override
	public void start(Long maxSize) {
		this.maxSize = maxSize;
		ex.execute(loop);
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

	private void fireProgress() {
		//eventlistener clonen zodat geen exceptions bij assyncroon aanroepen van addprogresslistener en remove
		Vector<ProgressListener> clone;
		synchronized (listeners) {
			clone = (Vector<ProgressListener>) listeners.clone();
		}
		for(ProgressListener p : clone) {
			p.Progress(currentSize, maxSize);
		}

	};

	private void fireLogg() {
		//eventlistener clonen zodat geen exceptions bij assyncroon aanroepen van addprogresslistener en remove
		String[] log = new String[loggs.size()];
                log = loggs.toArray(log);
		Vector<ProgressListener> clone;
		synchronized (listeners) {
			clone = (Vector<ProgressListener>) listeners.clone();
		}

		for(ProgressListener p : clone) {
			p.log(log);
		}

		for(String s: log) {
                    loggs.remove(s);
		}

	};

	private void fireFinnish() {
		//eventlistener clonen zodat geen exceptions bij assyncroon aanroepen van addprogresslistener en remove
		Vector<ProgressListener> clone;
		synchronized (listeners) {
			clone = (Vector<ProgressListener>) listeners.clone();
		}

		for(ProgressListener p : clone) {
			p.finnish();
		}
	}

	private void fireFail() {
		//eventlistener clonen zodat geen exceptions bij assyncroon aanroepen van addprogresslistener en remove
		Vector<ProgressListener> clone;
		synchronized (listeners) {
			clone = (Vector<ProgressListener>) listeners.clone();
		}

		for(ProgressListener p : clone) {
			p.fail();
		}

	}

	private class EventLoop implements Runnable {
		@Override
		public void run() {
			Long size = 0L;

			while(!finnish || !fail) {
				if(size != currentSize) {
					size = currentSize;
					fireProgress();
				}

				if(loggs.size() > 0) {
					fireLogg();
				}


				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			if(fail) {
                            fireFail();
			} else {
                            fireFinnish();
                        }
                        
                        try {
                            Thread.sleep(1000);
			} catch (InterruptedException e) {
                            e.printStackTrace();
			}


		}
	}
}
