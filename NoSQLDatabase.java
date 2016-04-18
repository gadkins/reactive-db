package reactive_nosql;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.json.*;

public class NoSQLDatabase extends Observable {

	private Map<String,Object> map;
	private File log;
	private static final String DEFAULT_LOG_NAME = "commands.txt";
	private static final String DEFAULT_SNAPSHOT_NAME = "dbSnapshot.txt";
	private Lock writeLock = new ReentrantReadWriteLock().writeLock();
	private Lock readLock = new ReentrantReadWriteLock().readLock();
	private HashSet<Cursor> cursors = new HashSet<Cursor>();
	
	private NoSQLDatabase() {
		this.map = new HashMap<String,Object>();
		createCommandLog();
	}
	
	private NoSQLDatabase(Map<String,Object> map) {
		this.map = new HashMap<String,Object>(map);
		createCommandLog();
	}
	
	public int size() {
		return map.size();
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}
	
	public boolean containsKey(String key) {
		return map.containsKey(formatKey(key));
	}

	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}
	
	public NoSQLDatabase put(String key, int value) {
		writeLock.lock();
		try{
			map.put(formatKey(key), value);
			writeCommand(new AddCommand(key,value), this.log);
		} finally {
			writeLock.unlock();
		}
		this.notifyObservers(key);
		return this;
	}
	
	public NoSQLDatabase put(String key, double value) {
		writeLock.lock();
		try{
			map.put(formatKey(key), value);
			writeCommand(new AddCommand(key,value), this.log);
		} finally {
			writeLock.unlock();
		}
		this.notifyObservers(key);
		return this;
	}
	
	public NoSQLDatabase put(String key, String value) {
		if (value == null) {
			throw new NullPointerException();
		}
		writeLock.lock();
		try{
			map.put(formatKey(key), value);
			writeCommand(new AddCommand(key,value), this.log);
		} finally {
			writeLock.unlock();
		}
		this.notifyObservers(key);
		return this;
	}
	
	public NoSQLDatabase put(String key, JSONArray value) {
		if (value == null) {
			throw new NullPointerException();
		}
		writeLock.lock();
		try{
			map.put(formatKey(key), value);
			writeCommand(new AddCommand(key,value), this.log);
		} finally {
			writeLock.unlock();
		}
		this.notifyObservers(key);
		return this;
	}
	
	public NoSQLDatabase put(String key, JSONObject value) {
		if (value == null) {
			throw new NullPointerException();
		}
		writeLock.lock();
		try{
			map.put(formatKey(key), value);
			writeCommand(new AddCommand(key,value), this.log);
		} finally {
			writeLock.unlock();
		}
		this.notifyObservers(key);
		return this;
	}
	
	public NoSQLDatabase put(String key, Object value) {
		if (value == null) {
			throw new NullPointerException();
		}
		writeLock.lock();
		try{
			writeCommand(new AddCommand(key,value), this.log);
			map.put(formatKey(key), value);
		} finally {
			writeLock.unlock();
		}
		this.notifyObservers(key);
		return this;
	}
	
	private void putAll(Map<? extends String,? extends Object> m) {
		writeLock.lock();
		try {
			for (Map.Entry<? extends String,? extends Object> entry: m.entrySet()) {
				put(entry.getKey(), entry.getValue());
				this.notifyObservers(entry.getKey());
			}
		} finally {
			writeLock.unlock();
		}
	}
	
	public int getInt(String key) {
		readLock.lock();
		Object value = null;
		try {
			value = map.get(formatKey(key));
			if (!(value instanceof Integer)) {
				throw new ClassCastException();
			}
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		} finally {
			readLock.unlock();
		}
		writeCommand(new GetCommand(key), this.log);
		return (int)value;
	}
	
	public double getDouble(String key) {
		readLock.lock();
		Object value = null;
		try {
			value = map.get(formatKey(key));
			if (!(value instanceof Double)) {
				throw new ClassCastException();
			}
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		} finally {
			readLock.unlock();
		}
		writeCommand(new GetCommand(key), this.log);
		return (double)value;
	}
	
	public String getString(String key) {
		readLock.lock();
		Object value = null;
		try {
			value = map.get(formatKey(key));
			if (!(value instanceof String)) {
				throw new ClassCastException();
			}
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		} finally {
			readLock.unlock();
		}
		writeCommand(new GetCommand(key), this.log);
		return (String)value;
	}
	
	public JSONArray getJSONArray(String key) {
		readLock.lock();
		Object value = null;
		try {
			value = map.get(formatKey(key));
			if (!(value instanceof JSONArray)) {
				throw new ClassCastException();
			}
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		} finally {
			readLock.unlock();
		}
		writeCommand(new GetCommand(key), this.log);
		return (JSONArray)value;
	}
	
	public JSONObject getJSONObject(String key) {
		readLock.lock();
		Object value = null;
		try {
			value = map.get(formatKey(key));
			if (!(value instanceof JSONObject)) {
				throw new ClassCastException();
			}
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		} finally {
			readLock.unlock();
		}
		writeCommand(new GetCommand(key), this.log);
		return (JSONObject)value;
	}
	
	public Object get(String key) {
		readLock.lock();
		Object value = null;
		try {
			value = map.get(formatKey(key));
		} catch (NoSuchElementException e) {
			System.out.println("NoSuchElementException: " + e.getMessage());
		} finally {
			readLock.unlock();
		}
		writeCommand(new GetCommand(key), this.log);
		return value;
	}

	public NoSQLDatabase getCopy() {
		NoSQLDatabase db = new NoSQLDatabase();
		db.putAll(this.map);
		return db;
	}
	
	public Cursor getCursor(String key) {
		Cursor c = Cursor.initCursor(this, key, this.get(key));
		cursors.add(c);
		return c;
	}
	
	public Object remove(String key) {
		writeLock.lock();
		Object value = null;
		try {
			value = map.remove(formatKey(key));
			writeCommand(new RemoveCommand(key), this.log);
		} finally {
			writeLock.unlock();
		}
		this.notifyObservers(key);
		return value;
	}
	
	public NoSQLDatabase replace(String key, Object value) {
		writeLock.lock();
		if (value == null) {
			throw new NullPointerException();
		}
		try {
			map.put(formatKey(key), value);
			writeCommand(new ReplaceCommand(key,value), this.log);
		} finally {
			writeLock.unlock();
		}
		this.notifyObservers(key);
		return this;
	}

	public void clear() {
		map.clear();
	}

	public Set<String> keySet() {
		return map.keySet();
	}

	public Collection<Object> values() {
		return map.values();
	}

	public Set<Map.Entry<String,Object>> entrySet() {
		return map.entrySet();
	}
	
	private void createCommandLog() {
		this.log = new File(DEFAULT_LOG_NAME);
		writeStringToFile("", log);
	}
	
	private String formatKey(String key) {
		if (key == null) {
			return null;
		}
		return key.toString().toLowerCase();
	}
	
	public Transaction initTransaction() {
		return new Transaction(this.getCopy());
	}
	
	@Override
	public void notifyObservers(Object key) {
		for (Cursor c: cursors) {
			c.update(this,key);
		}
	}
	
	public void snapshot() {
		String jsonDB = new JSONObject(map).toString(4);
		writeStringToFile(jsonDB, new File(DEFAULT_SNAPSHOT_NAME));
	}
	
	public void snapshot(File commands, File snapshot) {
		String jsonDB = new JSONObject(map).toString(4);
		writeStringToFile(jsonDB, snapshot);
		writeStringToFile("", commands);
	}
	
	private void writeCommand(NoSQLCommand cmd, File log) {
		writeStringToFile(cmd.toString() + "\n",log);
	}
	
	public static NoSQLDatabase recover() {
		return new NoSQLDatabase();
	}
	
	public static NoSQLDatabase recover(File commands, File snapshot) {
		NoSQLDatabase db = restoreFromSnapshot(snapshot);
		return restoreFromLog(commands, db);
	}
	
	private static final List<NoSQLCommand> readCommands(File commands) {
		List<NoSQLCommand> cmdList = new ArrayList<NoSQLCommand>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(commands));
			String line;
			while ((line = reader.readLine()) != null) {
				JSONObject entry = new JSONObject(line);
				Iterator<String> names = entry.keys();
				String cmdName = names.next();
				JSONObject cmdVal = entry.getJSONObject(cmdName);
				Iterator<String> it = cmdVal.keys();
				String key = it.next();
				Object value = null;
				if (!(cmdVal.isNull(key))) {
					value = cmdVal.get(key);
				}
				switch(cmdName) {
					case "ADD":
						cmdList.add(new AddCommand(key,value));
						break;
					case "GET":
						cmdList.add(new GetCommand(key));
						break;
					case "GET_INT":
						cmdList.add(new GetIntCommand(key));
						break;
					case "GET_DOUBLE":
						cmdList.add(new GetDoubleCommand(key));
						break;
					case "GET_STRING":
						cmdList.add(new GetStringCommand(key));
						break;
					case "GET_ARRAY":
						cmdList.add(new GetArrayCommand(key));
						break;
					case "GET_OBJECT":
						cmdList.add(new GetObjectCommand(key));
						break;
					case "REMOVE":
						cmdList.add(new RemoveCommand(key));
						break;
					case "REPLACE":
						cmdList.add(new ReplaceCommand(key,value));
						break;
					default:
						break;
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cmdList;
	}
	
	private static NoSQLDatabase restoreFromLog(File commands, NoSQLDatabase db) {
		List<NoSQLCommand> cmdList = readCommands(commands);
		for (NoSQLCommand c: cmdList) {
			c.execute(db);
		}
		return db;
	}
	
	private static NoSQLDatabase restoreFromSnapshot(File snapshot) {
		try {
			byte[] encodedFile = Files.readAllBytes(snapshot.toPath());
			String jsonDB = new String(encodedFile, StandardCharsets.UTF_8);
			return new NoSQLDatabase(jsonToMap(jsonDB));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static Map<String,Object> jsonToMap(String jsonString) {
		JSONObject obj = new JSONObject(jsonString);
		Iterator<String> it = obj.keys();
		String key;
		Map<String,Object> newMap = new HashMap<String,Object>();
		while(it.hasNext()) {
			key = it.next();
			newMap.put(key, obj.get(key));
		}
		return newMap;
	}
	
	private static void writeStringToFile(String contents, File filename) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true));
			writer.write(contents);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
