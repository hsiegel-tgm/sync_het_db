package test;

import static org.junit.Assert.assertEquals;
import mysql.MysqlConnection;

import org.junit.Test;

import balancer.SyncServer;

public class MysqlTest {
	@Test
	public void testloggerempty() {
		//I used:  mysql vsdb_03 localhost usr_vsdb03 pw_vsdb03

		MysqlConnection connection = new MysqlConnection("vsdb_03","localhost","usr_vsdb03","pw_vsdb03");
		assertEquals(connection.getLoggerCount(),0);
	 } 
	
	@Test
	public void testInsert() {
		//I used:  mysql vsdb_03 localhost usr_vsdb03 pw_vsdb03

		MysqlConnection connection = new MysqlConnection("vsdb_03","localhost","usr_vsdb03","pw_vsdb03");
		connection.execUpdate("INSERT INTO Person VALUES('Michi','Siegel','HR','bei Hannah')");
		assertEquals(connection.getLoggerCount(),1);
	 } 
	
	
	@Test
	public void testIfEntryExists() {
		//I used:  mysql vsdb_03 localhost usr_vsdb03 pw_vsdb03

		MysqlConnection connection = new MysqlConnection("vsdb_03","localhost","usr_vsdb03","pw_vsdb03");
		
		assertEquals(connection.isInDB("Abteilung","aname","IT"),true);
		assertEquals(connection.isInDB("Abteilung","aname","bloeeedsinn"),false);
		assertEquals(connection.isInDB("Person","vorname","Hannah"),true);
		assertEquals(connection.isInDB("Person","nachname","Hannah"),false);
	 } 
}
