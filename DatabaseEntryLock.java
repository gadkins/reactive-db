package reactive_nosql;

import java.util.concurrent.Semaphore;

public class DatabaseEntryLock {

	private DatabaseEntryLock() {}
	
	private static class SingletonHolder {
		private final static DatabaseEntryLock INSTANCE = new DatabaseEntryLock();
	}
	
	public static DatabaseEntryLock getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	private Semaphore writeLock = new Semaphore(1);
	
	public void getWriteLock() throws InterruptedException {
		writeLock.acquire();
	}
	
	public void releaseWriteLock() throws InterruptedException {
		writeLock.release();
	}
}
