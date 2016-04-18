package reactive_nosql;

import java.util.Iterator;

import org.json.JSONObject;

public class GetCommand implements NoSQLCommand {

	private String key;
	public static final String COMMAND_KEY = "GET";
	
	public GetCommand(String key) {
		this.key = key;
	}
	
	@Override
	public NoSQLDatabase execute(NoSQLDatabase db) {
		db.get(key);
		return db;
	}
	
	@Override
	public void rollback(NoSQLDatabase db) {
		// do nothing
	}
	
	@Override
	public String getName() {
		return COMMAND_KEY;
	}
	
	public String toString() {
		JSONObject obj = new JSONObject().put(COMMAND_KEY, new JSONObject().put(key,JSONObject.NULL));
		return obj.toString();
	}
	
	public GetCommand fromString(String str) {
		JSONObject obj = new JSONObject(str);
		JSONObject cmdVal = obj.getJSONObject(COMMAND_KEY);
		Iterator<String> it = cmdVal.keys();
		String key = it.next();
		return new GetCommand(key);
	}
}
