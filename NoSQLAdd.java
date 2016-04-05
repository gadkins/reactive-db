package reactive_nosql;

public class NoSQLAdd implements NoSQLCommand {

	NoSQLDatabase db;
	
	public NoSQLAdd(String key, String value) {
		this.db = db;
	}
	
	@Override
	public void execute() {
		db.add();

	}

}
