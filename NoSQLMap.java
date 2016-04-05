package reactive_nosql;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import org.json.simple.*;

public class NoSQLMap<K,V> implements Map<String,String>, Cloneable, Serializable {
	
	private Map<String,String> map = new HashMap<String,String>();
	private CommandLog log;
	
	public NoSQLMap() {
		this.log = new CommandLog(map);
	}
	
	private String formatKey(Object key) {
		if (key == null) {
			return null;
		}
		return key.toString().toLowerCase();
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
	public String get(Object key) {
		return map.get(formatKey(key));
	}

	@Override
	public String put(String key, String value) {
		return map.put(formatKey(key), value);
	}

	@Override
	public String remove(Object key) {
		return map.remove(formatKey(key));
	}

	@Override
	public void putAll(Map<? extends String,? extends String> m) {
		for (Map.Entry<? extends String,? extends String> entry: m.entrySet()) {
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
	public Collection<String> values() {
		return map.values();
	}

	@Override
	public Set<Map.Entry<String,String>> entrySet() {
		return map.entrySet();
	}
}
