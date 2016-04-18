package reactive_nosql;


import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.json.*;
import org.junit.*;

import static org.hamcrest.CoreMatchers.instanceOf;

public class NoSQLDatabaseTest {

	NoSQLDatabase db;
	JSONArray arr;
	JSONObject obj;
	
	@Before
	public void setup() {
		try {
			PrintWriter writer = new PrintWriter("commands.txt");
			writer.write("");
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		this.db = NoSQLDatabase.recover();
		this.arr = new JSONArray();
		this.obj = new JSONObject();
		arr.put("hello, world")
			.put("how are ya?")
			.put(123)
			.put(3.21)
			.put(new JSONArray())
			.put(new JSONObject());
		obj.put("foo", arr);
		clearSnapshot("dbSnapshot.txt");
	}
	
	@After
	public void cleanup() {
		db.clear();
	}
	
	public void clearSnapshot(String snapshot) {
		try {
			PrintWriter writer = new PrintWriter(snapshot);
			writer.write("");
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testPut() {
		db.put("a", 1);
		assertThat(db.getInt("a"), instanceOf(int.class));
		assertEquals(1, db.getInt("a"));
		
		db.put("b", 1.5678);
		assertThat(db.getDouble("b"), instanceOf(double.class));
		assertEquals(1.5678, db.getDouble("b"), 0.001); // max difference = 0.1%
		
		db.put("c", "hello, world");
		assertThat(db.getString("c"), instanceOf(String.class));
		assertEquals("hello, world", db.getString("c"));
		
		db.put("d", arr);
		Object result1 = db.getJSONArray("d");
		assertThat(result1, instanceOf(JSONArray.class));
		assertEquals("[\"hello, world\",\"how are ya?\",123,3.21,[],{}]", result1.toString());
		
		db.put("e", obj);
		Object result2 = db.getJSONObject("e");
		assertThat(result2, instanceOf(JSONObject.class));
		assertEquals("{\"foo\":[\"hello, world\",\"how are ya?\",123,3.21,[],{}]}", result2.toString());
	}
	
	@Test
	public void testGet() {
		testPut();
		assertNull(db.get("z"));
		db.remove("a");
		assertNull(db.get("a"));
	}
	
	@Test
	public void testReplace() {
		testPut();
		db.replace("a", 8);
	}
	
	@Test
	public void testRemove() {
		testPut();
		assertEquals(1.5678, db.remove("b"));
		assertEquals(1, db.remove("a"));
		assertEquals("hello, world", db.remove("c"));
		assertNull(db.remove("a"));
	}
	
	@Test
	public void testSnapshot() {
		testPut();
		File originalSnapshot = new File("testSnapshot.txt");
		db.snapshot(new File("commands.txt"), originalSnapshot);
		db.snapshot();
		try {
			File snapshot = new File("dbSnapshot.txt");
			byte[] encodedFile1 = Files.readAllBytes(snapshot.toPath());
			byte[] encodedFile2 = Files.readAllBytes(originalSnapshot.toPath());
			String jsonDB = new String(encodedFile1, StandardCharsets.UTF_8);
			String jsonOrig = new String(encodedFile2, StandardCharsets.UTF_8);
			assertEquals(jsonOrig,jsonDB);
		} catch (IOException e) {
			e.printStackTrace();
		}
		clearSnapshot("dbSnapshot.txt");
	}
	
	@Test
	public void testRecover() {
		testPut();
		db.snapshot();
		File commands = new File("commands.txt");
		File snapshot = new File("dbSnapshot.txt");
		db.put("x", "foobar");
		NoSQLDatabase existingDB = NoSQLDatabase.recover(commands, snapshot);
		assertEquals(1,existingDB.getInt("a"));
		assertEquals(1.5678, existingDB.getDouble("b"), 0.001);
		assertEquals("foobar", existingDB.getString("x"));
//		existingDB.snapshot();
		clearSnapshot("dbSnapshot.txt");
	}
	
}
