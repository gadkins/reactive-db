package reactive_nosql;

public class BankingApplication {
	
	public BankingApplication() {
		NoSQLDatabase db = NoSQLDatabase.recover();
	}
	
	
//	private NoSQLDatabase<String,String> retrieveDatabase() {
//		// Restore DB from momento - check for existence
//		// Write to DB from command - check for existence
//	}
	
	private void logTransaction() {
		
	}
	
	public void makeTransation(String issuer, String receipient, 	Number transactionAmt) {
		
	}
}
