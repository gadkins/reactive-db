package reactive_nosql;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Acts as observer of NoSQLDatabase and Observable by CursorTest
 * Is Proxy between NoSQLDatabase and CursorTest
 */
public class Cursor extends Observable implements Observer {
	
	private NoSQLDatabase db;
	private final String key;
	private Object value;
	private List<Observer> observers = new LinkedList<Observer>();
	private boolean changed;
	
	private Cursor(NoSQLDatabase db, String key, Object value) {
		this.db = db;
		this.key = key;
		this.value = value;
		changed = false;
	}
	
	public static Cursor initCursor(NoSQLDatabase db, String key, Object value) {
		return new Cursor(db, key, value);
	}
	
	public int getInt() {	
		return db.getInt(key);
	}
	
	public double getDouble() {
		return db.getDouble(key);
	}
	
	public String getString() {
		return db.getString(key);
	}
	
	public JSONArray getJSONArray() {
		return db.getJSONArray(key);
	}
	
	public JSONObject getJSONObject() {
		return db.getJSONObject(key);
	}
	
	public Object get() {
		return db.get(key);
	}
	
	@Override
	public void addObserver(Observer o) {
		observers.add(o);
	}
	
	@Override
	protected void clearChanged() {
		changed = false;
	}
	
	@Override
	public int countObservers() {
		return observers.size();
	}
	
	@Override
	public boolean hasChanged() {
		return changed;
	}
	
	@Override
	public void notifyObservers(Object key) {
		if (hasChanged()) {
			for (Observer o: observers) {
				o.update(this, value);
			}
			clearChanged();
		}
	}
	
	public boolean removeObserver(Observer o) {
		return observers.remove(o);
	}
	
	@Override
	public void setChanged() {
		changed = true;
	}

	@Override
	public void update(Observable o, Object key) {
		if (this.key.equals(key)) {
			setChanged();
			this.notifyObservers(key);
		}
		
	}
}
