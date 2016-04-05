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

import org.junit.*;

public class NoSQLDatabaseTest {

//	@Test
//	public void testGetConnection() {
//		NoSQLDatabase testDB = new NoSQLDatabase<String, Integer>();
//		DummyConnection conn = testDB.getConnection();
//	}
	private static NoSQLDatabase<String,Object> db = new NoSQLDatabase<String,Object>();
	private static List<String> array = new ArrayList<String>();
	private static Map<String,Object> object = new HashMap<String,Object>();

	@Before
	public void setup() {
		array.add("hello, world");
		array.add("how are ya?");
		object.put("1", array);
		object.put("2", new HashMap<String,Object>());
	}
	
	@AfterClass
	public static void teardown() {
		System.out.println("Writing database to file...");
		FileWriter fstream;
	    BufferedWriter out;
	    try {
		    fstream = new FileWriter("database.txt");
		    out = new BufferedWriter(fstream);
	
		    Iterator<Entry<String, String>> it = db.entrySet().iterator();
	
		    while (it.hasNext()) {
		        Map.Entry<String, String> pairs = it.next();
		        out.write(pairs.getKey() + ", ");
		        out.write(pairs.getValue() + "\n");
		    }
		    out.flush();
		    out.close();
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	}
	
	@Test
	public void testAdd() {
		db.add("a1", 1);
		db.add("a2", 1.23);
		db.add("b1", "hello");
		db.add("c1", array);
		db.add("d1", object);
	}
	
	@Test
	public void testGet() {
		String obj1 = db.get("a1");
		String obj2 = db.get("a2");
		String obj3 = db.get("b1");
		String obj4 = db.get("c1");
		String obj5 = db.get("d1");
		assertEquals("1", obj1);
		assertEquals("1.23", obj2);
		assertEquals("\"hello\"", obj3);
		assertEquals("[\"hello, world\",\"how are ya?\"]", obj4);
		assertEquals("{\"1\":[\"hello, world\",\"how are ya?\"],\"2\":{}}", obj5);
		try {
			PrintWriter file = new PrintWriter("test.txt");
			file.println(obj1);
			file.println(obj2);
			file.println(obj3);
			file.println(obj4);
			file.println(obj5);
			file.flush();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testReplace() {
		db.replace("a1", 8);
		assertEquals("8", db.get("a1"));
		ArrayList<Integer> intList = new ArrayList<Integer>();
		intList.add(5);
		intList.add(4);
		intList.add(3);
		db.replace("b1", intList);
		assertEquals("[5,4,3]", db.get("b1"));
	}
	
	@Test
	public void testRemove() {
//		db.remove("a1");
//		assertNull(db.get("a1"));
	}

}
