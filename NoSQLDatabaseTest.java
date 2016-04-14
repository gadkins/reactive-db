package reactive_nosql;

import com.google.gson.*;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.junit.*;
import static org.hamcrest.CoreMatchers.instanceOf;

public class NoSQLDatabaseTest {

//	@Test
//	public void testGetConnection() {
//		NoSQLDatabase testDB = new NoSQLDatabase<String, Integer>();
//		DummyConnection conn = testDB.getConnection();
//	}
//	private static NoSQLDatabase db = new NoSQLDatabase();
//	private static List<String> array = new ArrayList<String>();
//	private static Map<String,Object> object = new HashMap<String,Object>();

	@BeforeClass
	public static void setup() {
//		array.add("hello, world");
//		array.add("how are ya?");
//		object.put("1", array);
//		object.put("2", new HashMap<String,Object>());
	}
	
	@AfterClass
	public static void teardown() {
		NoSQLDatabase db = NoSQLDatabase.recover();
		List<String> array = new ArrayList<String>();
		Map<String,Object> object = new HashMap<String,Object>();
		array.add("hello, world");
		array.add("how are ya?");
		object.put("1", array);
		object.put("2", new HashMap<String,Object>());
		db.put("a1", 1)
			.put("a2", 1.23)
			.put("b1", "hello")
			.put("c1", array)
			.put("d1", object);
		System.out.println("Writing database to file...");
		FileWriter fstream;
	    BufferedWriter out;
	    try {
		    fstream = new FileWriter("database.txt");
		    out = new BufferedWriter(fstream);
	
		    Iterator<Entry<String, Object>> it = db.entrySet().iterator();
	
		    while (it.hasNext()) {
		        Map.Entry<String, Object> pairs = it.next();
		        String key = pairs.getKey();
		        Object value = pairs.getValue();
		        out.write(new Gson().toJson(key) + ", ");
		        out.write(new Gson().toJson(value) + "\n");
		    }
		    out.flush();
		    out.close();
		    fstream.close();
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	}
	
	@Test
	public void testPut() {
		NoSQLDatabase db = NoSQLDatabase.recover();
		List<String> array = new ArrayList<String>();
		Map<String,Object> object = new HashMap<String,Object>();
		JSONArray jsArr = new JSONArray();
		array.add("hello, world");
		array.add("how are ya?");
		object.put("1", array);
		object.put("2", new HashMap<String,Object>());
		db.put("a1", 1)
			.put("a2", 1.23)
			.put("b1", "hello")
			.put("c1", array)
			.put("d1", object)
			.put("e1", jsArr);
	}
	
	@Test
	public void testGet() {
		NoSQLDatabase db = NoSQLDatabase.recover();
		List<String> array = new ArrayList<String>();
		Map<String,Object> object = new HashMap<String,Object>();
		array.add("hello, world");
		array.add("how are ya?");
		object.put("1", array);
		object.put("2", new HashMap<String,Object>());
		db.put("a1", 1)
		.put("a2", 1.23)
		.put("b1", "hello")
		.put("c1", array)
		.put("d1", object);
		Object obj1 = db.get("a1");
		Object obj2 = db.get("a2");
		Object obj3 = db.get("b1");
		Object obj4 = db.get("c1");
		Object obj5 = db.get("d1");
		assertEquals(1, obj1);
		assertEquals(1.23, obj2);
		assertEquals("hello", obj3);
		assertThat(obj4, instanceOf(ArrayList.class));
//		assertEquals("[hello, world, how are ya?]", obj4);
//		assertEquals("{\"1\":[\"hello, world\",\"how are ya?\"],\"2\":{}}", obj5);
		try {
			PrintWriter file = new PrintWriter("test.txt");
			file.println(obj1);
			file.println(obj2);
			file.println(obj3);
			file.println(obj4);
//			file.println(obj5);
			file.flush();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testReplace() {
//		db.replace("a1", 8);
//		assertEquals("8", db.get("a1"));
//		ArrayList<Integer> intList = new ArrayList<Integer>();
//		intList.add(5);
//		intList.add(4);
//		intList.add(3);
//		db.replace("b1", intList);
//		assertEquals("[5,4,3]", db.get("b1"));
	}
	
	@Test
	public void testRemove() {
//		db.remove("a1");
//		assertNull(db.get("a1"));
	}

}
