package reactive_nosql;

import java.util.Iterator;

import org.json.JSONObject;

public class AddCommand implements NoSQLCommand {

	private String key;
	private Object value;
	public static final String COMMAND_KEY = "ADD";
	
	public AddCommand(String key, Object value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public NoSQLDatabase execute(NoSQLDatabase db) {
		return db.put(key, value);
	}
	
	@Override
	public void rollback(NoSQLDatabase db) {
		db.remove(key);
	}
	
	public String toString() {
		JSONObject obj = new JSONObject().put(COMMAND_KEY, new JSONObject().put(key,value));
		return obj.toString();
	}
	
	public AddCommand fromString(String str) {
		JSONObject obj = new JSONObject(str);
		JSONObject cmdVal = obj.getJSONObject(COMMAND_KEY);
		Iterator<String> it = cmdVal.keys();
		String key = it.next();
		return new AddCommand(key, cmdVal.get(key));
	}
	
	public String getName() {
		return COMMAND_KEY;
	}
}
