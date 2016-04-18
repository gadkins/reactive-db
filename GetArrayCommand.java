package reactive_nosql;

import java.util.Iterator;

import org.json.JSONObject;

public class GetArrayCommand implements NoSQLCommand {

	private String key;
	public static final String COMMAND_KEY = "GET_ARRAY";
	
	public GetArrayCommand(String key) {
		this.key = key;
	}
	
	@Override
	public NoSQLDatabase execute(NoSQLDatabase db) {
		db.getJSONArray(key);
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
	
	public GetArrayCommand fromString(String str) {
		JSONObject obj = new JSONObject(str);
		JSONObject cmdVal = obj.getJSONObject(COMMAND_KEY);
		Iterator<String> it = cmdVal.keys();
		String key = it.next();
		return new GetArrayCommand(key);
	}
}
