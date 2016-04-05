package reactive_nosql;

public class DummyConnection {

	private int count = 0;
	private  DummyConnection() {}
	
	private static class SingletonConnection {
		private final static DummyConnection INSTANCE = new DummyConnection();
	}
	
	public static DummyConnection instance() {
		return SingletonConnection.INSTANCE;
	}
	
	public int increase() {
		return ++count;
	}
	
	public boolean close() {
		return true;
	}
	
}
