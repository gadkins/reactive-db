package reactive_nosql;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;

import com.google.gson.Gson;

public class DatabaseSnapshot {
	
//	private NoSQLDatabase db;
//	private File snapshot;
	
	private DatabaseSnapshot(File snapshot) {
//		this.snapshot = snapshot;
		
	}
	
//	public static final NoSQLDatabase restoreDatabase(File snapshot) {
//		try {
//			byte[] encodedFile = Files.readAllBytes(snapshot.toPath());
//			String jsonDatabase = new String(encodedFile, StandardCharsets.UTF_8);
//			return new Gson().fromJson(jsonDatabase, NoSQLDatabase.class);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//	
//	public static final File snapshot() {
//		
//	}
//	
//	public boolean exists(File filename) {
//		if (filename.exists() && !filename.isDirectory()) {
//			return true;
//		} else {
//			return false;
//		}
//	}
}
