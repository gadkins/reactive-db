package reactive_nosql;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import org.json.JSONObject;


public class DatabaseSnapshot {
	
	private HashMap<String,File> savedState = new HashMap<String,File>();
	
	protected DatabaseSnapshot() {}
	
	public void setState(String stateName, Object stateValue) {
		String jsonString = new JSONObject(savedState).toString(4);
		File snapshot = writeStringToFile(jsonString, new File("database_clone.txt"));
		savedState.put(stateName, snapshot);
	}
	
	public Object getState(String stateName) {
		return savedState.get(stateName);
	}
	
	private File writeStringToFile(String contents, File filename) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true));
			writer.write(contents);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return filename;
	}
}
