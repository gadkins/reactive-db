package reactive_nosql;

import java.util.ArrayList;
import java.util.List;

import org.json.*;
import org.omg.CORBA.portable.UnknownException;

public class Transaction {

	private NoSQLDatabase db;
	private List<NoSQLCommand> sequence = new ArrayList<NoSQLCommand>();
	private boolean active;
	
	protected Transaction(NoSQLDatabase db) {
		this.db = db;
		this.active = true;
	}
	
	public NoSQLDatabase put(String key, Object value) {
		try {
			db.put(key, value);
			sequence.add(new AddCommand(key,value));
		} catch (UnknownException e) {
			e.printStackTrace();
			abort();
		} 
		return db;
	}
	
	public int getInt(String key) {
		Object value = null;
		try {
			value = db.getInt(key);
			sequence.add(new GetIntCommand(key));
		} catch (UnknownException e) {
			e.printStackTrace();
			abort();
		} 
		return (int)value;
	}
	
	public double getDouble(String key) {
		Object value = null;
		try {
			value = db.getDouble(key);
			sequence.add(new GetDoubleCommand(key));
		} catch (UnknownException e) {
			e.printStackTrace();
			abort();
		} 
		return (double)value;
	}
	
	public String getString(String key) {
		Object value = null;
		try {
			value = db.getString(key);
			sequence.add(new GetStringCommand(key));
		} catch (UnknownException e) {
			e.printStackTrace();
			abort();
		} 
		return (String)value;
	}
	
	public JSONArray getJSONArray(String key) {
		Object value = null;
		try {
			value = db.getJSONArray(key);
			GetArrayCommand cmd = new GetArrayCommand(key);
			sequence.add(cmd);
		} catch (UnknownException e) {
			e.printStackTrace();
			abort();
		} 
		return (JSONArray)value;
	}
	
	public JSONObject getJSONObject(String key) {
		Object value = null;
		try {
			value = db.getJSONObject(key);
			sequence.add(new GetObjectCommand(key));
		} catch (UnknownException e) {
			e.printStackTrace();
			abort();
		} 
		return (JSONObject)value;
	}
	
	public Object get(String key) {
		Object value = null;
		try {
			value = db.get(key);
			sequence.add(new GetCommand(key));
		} catch (UnknownException e) {
			e.printStackTrace();
			abort();
		} 
		return value;
	}
	
	public Object remove(String key) {
		Object value = null;
		try {
			RemoveCommand cmd = new RemoveCommand(key);
			cmd.execute(db); // need to set the value that was removed to support abort/rollback
			sequence.add(cmd);
		} catch (UnknownException e) {
			e.printStackTrace();
			abort();
		} 
		return (JSONArray)value;
	}
	
	public void commit() {
		if (active) {
			sequence = null;
		}
		active = false;
	}
	
	public void abort() {
		if (active) {
			for (NoSQLCommand c: sequence) {
				c.rollback(db);
			}
		}
		active = false;
	}
	
	public boolean isActive() {
		return active;
	}
	
}
