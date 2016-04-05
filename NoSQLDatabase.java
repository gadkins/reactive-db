package reactive_nosql;

import com.google.gson.*;


public class NoSQLDatabase<K,V> extends NoSQLMap<String,V> {
	
	private CommandLog log;
	
	public NoSQLDatabase() {
		super();
		this.log = new CommandLog(this);
	}
	
	public String add(String k, Object str) {
		return this.put(k, new Gson().toJson(str));
	}
	
	public String get(String k) {
		return this.get(new Gson().fromJson(k, Object.class));
	}
	
	public String replace(String k, Object v) {
		return this.put(k, new Gson().toJson(v));
	}
	
	public String remove(String k) {
		return this.remove(k);
	}
}
