package reactive_nosql;

import static org.junit.Assert.*;

import org.junit.Test;

public class TransactionTest {

	@Test
	public void testAbort() {
		NoSQLDatabase db = NoSQLDatabase.recover();
		Transaction tr = db.initTransaction();
		db.put("abcBank", 5000);
		try {
			tr.put("1", 100);
			tr.put("2", 200);
			assertTrue(tr.isActive());
			tr.get("1");
			tr.remove("1");
			int cash = tr.getInt("abcBank");
			if (cash < 100000) {
				tr.abort();
			} else {
				tr.commit();
			}
			assertNull(db.getInt("2"));
			assertNull(db.getInt("1"));
		} catch (Exception e) {
			tr.abort();
		}
	}
	
	@Test
	public void testCommit() {
		NoSQLDatabase db = NoSQLDatabase.recover();
		Transaction tr = db.initTransaction();
		db.put("abcBank", 5000);
		try {
			tr.put("1", 100);
			tr.put("2", 200);
			tr.get("1");
			tr.remove("1");
			int cash = tr.getInt("abcBank");
			if (cash < 10) {
				tr.abort();
				assertFalse(tr.isActive());
			} else {
				assertTrue(tr.isActive());
				tr.commit();
				assertFalse(tr.isActive());
			}
			assertEquals(200, db.getInt("2"));
			assertEquals(100, db.getInt("1"));
		} catch (Exception e) {
			tr.abort();
		}
	}

}
