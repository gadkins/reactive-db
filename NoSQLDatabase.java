package reactive_nosql;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

import com.google.gson.Gson;

public class NoSQLDatabase implements Map<String,Object>, Cloneable, Serializable {
	
	private static final long serialVersionUID = 1L;
	private Map<String,Object> map;
	private File log;
	private static final String DEFAULT_LOG_NAME = "commands.txt";
	private static final String DEFAULT_SNAPSHOT_NAME = "dbSnapshot.txt";
	
	private NoSQLDatabase() {
		this.map = new HashMap<String,Object>();
		createCommandLog();
	}
	
	@Override
	public int size() {
		return map.size();
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return map.containsKey(formatKey(key));
	}

	@Override
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	@Override
	public Object get(Object key) {
		Object value = null;
		try {
			value = map.get(formatKey(key));
		} catch (NoSuchElementException e) {
			System.out.println("NoSuchElementException: " + e.getMessage());
		}
		return value;
	}

	@Override
	public NoSQLDatabase put(String key, Object value) {
		if (value == null) {
			throw new NullPointerException();
		}
		map.put(formatKey(key), value);
//		log.writeToLog(new NoSQLAdd(key,value));
		writeCommand(new NoSQLAdd(key,value), this.log);
		return this;
	}

	@Override
	public Object remove(Object key) {
		return map.remove(formatKey(key));
	}

	@Override
	public void putAll(Map<? extends String,? extends Object> m) {
		for (Map.Entry<? extends String,? extends Object> entry: m.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public Set<String> keySet() {
		return map.keySet();
	}

	@Override
	public Collection<Object> values() {
		return map.values();
	}

	@Override
	public Set<Map.Entry<String,Object>> entrySet() {
		return map.entrySet();
	}
	
	private void createCommandLog() {
		this.log = new File(DEFAULT_LOG_NAME);
		writeStringToFile("", log);
	}
	
	private String formatKey(Object key) {
		if (key == null) {
			return null;
		}
		return key.toString().toLowerCase();
	}
	
	public void snapshot() {
		String jsonDB = new Gson().toJson(this);
		writeStringToFile(jsonDB, new File(DEFAULT_SNAPSHOT_NAME));
	}
	
	public void snapshot(File commands, File snapshot) {
		String jsonDB = new Gson().toJson(this);
		writeStringToFile(jsonDB, snapshot);
		writeStringToFile("", commands);
	}
	
	private void writeCommand(NoSQLCommand cmd, File log) {
		writeStringToFile(new Gson().toJson(cmd) + "\n", log);
	}
	
	public static NoSQLDatabase recover() {
		return new NoSQLDatabase();
	}
	
//	public static NoSQLDatabase recover(File commands) {
//		return restoreFromLog(commands, new NoSQLDatabase());
//	}
	
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
				cmdList.add(new Gson().fromJson(line, NoSQLCommand.class));
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
			String jsonDatabase = new String(encodedFile, StandardCharsets.UTF_8);
			return new Gson().fromJson(jsonDatabase, NoSQLDatabase.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static void writeStringToFile(String contents, File filename) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
			writer.write(contents);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
