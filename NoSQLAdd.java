package reactive_nosql;

import com.google.gson.Gson;

public class NoSQLAdd implements NoSQLCommand {

//	private NoSQLDatabase db;
	private String key;
	private Object value;
	public static final String COMMAND_NAME = "ADD";
	
	public NoSQLAdd(String key, Object value) {
//		this.db = db;
		this.key = key;
		this.value = value;
	}
	
	@Override
	public NoSQLDatabase execute(NoSQLDatabase db) {
		return db.put(key, value);
//		writeCommand();
	}
	
//	@Override
//	public void writeCommand() {
//		log.writeToLog(this);
//	}
	
	@Override
	public String toString() {
		return COMMAND_NAME + ": " + key + ", " + new Gson().toJson(value);
	}
	
}
