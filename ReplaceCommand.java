package reactive_nosql;

import java.util.Iterator;
import org.json.JSONObject;


public class ReplaceCommand implements NoSQLCommand {

	private String key;
	private Object value;
	public static final String COMMAND_KEY = "REPLACE";
	
	public ReplaceCommand(String key, Object value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public NoSQLDatabase execute(NoSQLDatabase db) {
		return db.replace(key, value);
	}
	
	@Override
	public void rollback(NoSQLDatabase db) {
		throw  new UnsupportedOperationException();
	}
	
	public String toString() {
		JSONObject obj = new JSONObject().put(COMMAND_KEY, new JSONObject().put(key,value));
		return obj.toString();
	}
	
	public ReplaceCommand fromString(String str) {
		JSONObject obj = new JSONObject(str);
		JSONObject cmdVal = obj.getJSONObject(COMMAND_KEY);
		Iterator<String> it = cmdVal.keys();
		String key = it.next();
		return new ReplaceCommand(key, cmdVal.get(key));
	}
	
	public String getName() {
		return COMMAND_KEY;
	}
}
