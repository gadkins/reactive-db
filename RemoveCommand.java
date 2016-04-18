package reactive_nosql;

import java.util.Iterator;

import org.json.JSONObject;

public class RemoveCommand implements NoSQLCommand {

	private String key;
	private Object value;
	public static final String COMMAND_KEY = "REMOVE";
	
	public RemoveCommand(String key) {
		this.key = key;
	}
	
	@Override
	public NoSQLDatabase execute(NoSQLDatabase db) {
		this.setValue(db.remove(key));
		return db;
	}
	
	@Override
	public void rollback(NoSQLDatabase db) {
		db.put(key,value);
	}
	
	public String toString() {
		JSONObject obj = new JSONObject().put(COMMAND_KEY, new JSONObject().put(key,JSONObject.NULL));
		return obj.toString();
	}
	
	public RemoveCommand fromString(String str) {
		JSONObject obj = new JSONObject(str);
		JSONObject cmdVal = obj.getJSONObject(COMMAND_KEY);
		Iterator<String> it = cmdVal.keys();
		String key = it.next();
		return new RemoveCommand(key);
	}
	
	public String getName() {
		return COMMAND_KEY;
	}

	public Object getValueRemoved() {
		return value;
	}

	private void setValue(Object value) {
		this.value = value;
	}
}
