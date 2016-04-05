package reactive_nosql;

import java.io.File;
import java.util.List;

public class CommandLog {

	protected final NoSQLMap<String,String> db;
	private List<NoSQLCommand> cmdList;
	
	public CommandLog(NoSQLMap<String,String> db) {
		this.db = db;
	}
	
	protected final void applyCommands() {
		
	}
	
	protected final void buildCommandList() {
		
	}
	
	protected final String parseCommand(File f) {
		
	}
	
	
}
