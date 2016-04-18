package reactive_nosql;

import static org.junit.Assert.*;

import java.util.Observable;
import java.util.Observer;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.*;

public class CursorTest implements Observer {

	NoSQLDatabase db;
	JSONArray arr;
	JSONObject obj;
	static boolean notified;
	
	@BeforeClass
	public static void start() {
		notified = false;
	}
	
	@Before
	public void setup() {
		this.db = NoSQLDatabase.recover();
		this.arr = new JSONArray();
		this.obj = new JSONObject();
		arr.put("hello, world")
			.put(123);
		obj.put("foo", arr);
		db.put("a", 1)
		.put("b", "hello");
	}
	
	@Test
	public void testAddObserver() {
		Cursor cursor = db.getCursor("a");
		Observer o1 = new CursorTest();
		cursor.addObserver(o1);
		assertEquals(1, cursor.countObservers());
		Observer o2 = new CursorTest();
		cursor.addObserver(o2);
		assertEquals(2, cursor.countObservers());
	}
	
	@Test
	public void testRemoveObserver() {
		Cursor cursor = db.getCursor("a");
		Observer o = new CursorTest();
		cursor.addObserver(o);
		assertEquals(1, cursor.countObservers());
		cursor.removeObserver(o);
		assertEquals(0, cursor.countObservers());
	}

	@Test public void testNotifyObservers() {
		db.put("bar", 10);
		Cursor data = db.getCursor("bar");
		assertEquals(10, data.get());
		db.replace("bar", "goodbye");
	}
	
	@Override
	public void update(Observable o, Object key) {
		assertEquals("goodbye", ((Cursor) o).get());
		assertTrue(o.hasChanged());
		notified = true;
	}

}
