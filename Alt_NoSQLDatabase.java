package reactive_nosql;

import java.util.NoSuchElementException;

import com.google.gson.*;


//public class Alt_NoSQLDatabase extends NoSQLDatabase<String,Object> {
//	
//	private static final long serialVersionUID = 1L;
//	private CommandLog log;
//	
//	public Alt_NoSQLDatabase() {
//		super();
//		this.log = new CommandLog(this);
//	}
//	
//	public Alt_NoSQLDatabase add(String key, Object value) {
//		if (value == null) {
//			throw new NoSuchElementException();
//		}
//		return (Alt_NoSQLDatabase) this.put(key,value);
//	}
//	
////	public String get(String k) {
////		return this.get(k);
////	}
//	
//	public Object replace(String k, Object v) {
//		return this.put(k, v);
//	}
//	
//	public String remove(String k) {
//		return this.remove(k);
//	}
//}
