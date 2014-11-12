package test;

import static org.junit.Assert.assertEquals;
import mysql.MysqlConnection;

import org.junit.Test;

import syncserver.SyncServer;

public class MysqlTest {
	@Test
	public void testloggerempty() {
		//I used:  mysql vsdb_03 localhost usr_vsdb03 pw_vsdb03
		MysqlConnection connection = new MysqlConnection("vsdb_03","localhost","usr_vsdb03","pw_vsdb03","mysql");
		connection.execUpdate("DELETE FROM Logged");
		assertEquals(connection.getLoggerCount(),0);
		connection.close();

	 } 
	
	@Test
	public void testInsert() {
		//I used:  mysql vsdb_03 localhost usr_vsdb03 pw_vsdb03
		MysqlConnection connection = new MysqlConnection("vsdb_03","localhost","usr_vsdb03","pw_vsdb03","mysql");
		connection.execUpdate("DELETE FROM Logged");
		connection.execUpdate("INSERT INTO Person VALUES('Vorname2','Kakakakakak','IT','bei Hannah',DEFAULT)");
		assertEquals(connection.getLoggerCount(),1);
		connection.close();
	 } 
	
	
	@Test
	public void testIfEntryExists() {
		//I used:  mysql vsdb_03 localhost usr_vsdb03 pw_vsdb03
		MysqlConnection connection = new MysqlConnection("vsdb_03","localhost","usr_vsdb03","pw_vsdb03","mysql");
		
		assertEquals(connection.isInDB("Abteilung","aname","IT"),true);
		assertEquals(connection.isInDB("Abteilung","aname","bloeeedsinn"),false);
		assertEquals(connection.isInDB("Person","vorname","Hannah"),true);
		assertEquals(connection.isInDB("Person","nachname","Hannah"),false);
		connection.close();

	 } 
	
}
