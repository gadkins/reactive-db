package reactive_nosql;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;

public class CommandLog {

	private List<NoSQLCommand> cmdList; // in-memory command list for easily restoring db from disk
	private File logFile;
	
	public CommandLog() {
		this(new File("commands.txt"));
	}
	
	public CommandLog(File logFile) {
		this.logFile = logFile;
//		executeCommandLog();
	}
	
	protected final NoSQLDatabase executeCommandLog(NoSQLDatabase db) { // should this method return a database
		readFromLog();
		for (NoSQLCommand c: cmdList) {
			c.execute(db);
		}
		return db;
	}
	
	private final void readFromLog() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(logFile));
			String line;
			while ((line = reader.readLine()) != null) {
				cmdList.add(new Gson().fromJson(line, NoSQLCommand.class));
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void writeToLog(NoSQLCommand cmd) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(logFile));
//			writer.write(cmd.toString() + "\n");
			writer.write(new Gson().toJson(cmd) + "\n");
			writer.flush();
			writer.close();
		}
		catch (IOException e){
			e.printStackTrace();
		}
//		cmdList.add(cmd);
	}
	
	protected final void clear() {
		cmdList = null;
	}
	
	protected final boolean exists(File filename) {
		if (filename.exists() && !filename.isDirectory()) {
			return true;
		} else {
			return false;
		}
	}
}
