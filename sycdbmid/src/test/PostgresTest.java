package test;

import static org.junit.Assert.assertEquals;
import mysql.MysqlConnection;

import org.junit.Test;

import postgres.PostgresConnection;

public class PostgresTest {
	@Test
	public void testloggerempty() {
		//192.168.232.128:5432/vsdb_03", "postgres","hermine11");
		PostgresConnection connection = new PostgresConnection("vsdb_03","192.168.232.128","postgres","hermine11","postgres");
		connection.execUpdate("DELETE FROM Logged");
		assertEquals(connection.getLoggerCount(),0);
		connection.close();

	 } 
	
	@Test
	public void testInsert() {
		//I used:  mysql vsdb_03 localhost usr_vsdb03 pw_vsdb03
		PostgresConnection connection = new PostgresConnection("vsdb_03","192.168.232.128","postgres","hermine11","postgres");
		connection.execUpdate("DELETE FROM Logged");
		connection.execUpdate("INSERT INTO Mitarbeiter VALUES('Michi Siegel','HR',DEFAULT)");
		assertEquals(connection.getLoggerCount(),1);
		connection.close();
	} 
	
	
	
	@Test
	public void testIfEntryExists() {
		//I used:  mysql vsdb_03 localhost usr_vsdb03 pw_vsdb03
		PostgresConnection connection = new PostgresConnection("vsdb_03","192.168.232.128","postgres","hermine11","postgres");
		
		assertEquals(connection.isInDB("Mitarbeiter","name","Hannah Siegel"),true);
		assertEquals(connection.isInDB("Mitarbeiter","name","Hannah"),false);
		connection.close();

	 } 
}
