package test;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import mysql.ChangeListenerMysql;
import mysql.MysqlConnection;
import mysql.MysqlServer;
import postgres.ChangeListenerPostgres;
import postgres.PostgresConnection;
import postgres.PostgresServer;
import syncserver.SyncServer;

public class TestSynchronisation {
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	private MysqlConnection connection;
	private PostgresConnection pgconnection;

	@Before
	public void runBeforeClass() {
		SyncServer a = new SyncServer();

		connection = new MysqlConnection("vsdb_03", "localhost", "usr_vsdb03","pw_vsdb03", "Mysql1");
		pgconnection = new PostgresConnection("vsdb_03", "192.168.232.128","postgres", "hermine11", "Postgres1");

		MysqlServer srv = new MysqlServer("Mysql1", connection, "127.0.0.1");
		PostgresServer srv2 = new PostgresServer("Postgres1", pgconnection," 127.0.0.1");
		ChangeListenerMysql clm = new ChangeListenerMysql("Mysql1", connection,"127.0.0.1");
		ChangeListenerPostgres clp = new ChangeListenerPostgres("Postgres1",pgconnection, "127.0.0.1");
	}

	@Before
	public void setUpStreams() {
	}

	// @After
	// public void cleanUpStreams() {
	// connection.close();
	// pgconnection.close();
	// }

	 @Test
	 public void postgresupdatePerson() {
		 pgconnection.execUpdate("UPDATE Mitarbeiter SET abteilung='Abteilung2',sync='new' WHERE name='Paul'");
		 try {
			 Thread.sleep(7000);
		 } catch (InterruptedException e) {}
		 
		 assertEquals(connection.isInDB("Person", "aname", "Abteilung2"),true);
		 assertEquals(connection.isInDB("Abteilung", "aname", "Abteilung2"),true);
		 assertEquals(pgconnection.isInDB("Mitarbeiter", "abteilung","Abteilung2"),true);
	 }

	@Test
	public void myqlinsertPerson() {
		connection.execUpdate("INSERT INTO Person VALUES('Susi','Mueller','HR','addresse wien',DEFAULT)");
		try {
			Thread.sleep(7000);
		} catch (InterruptedException e) {
		}
		assertEquals(pgconnection.isInDB("Mitarbeiter", "name", "Susi Mueller"),true);
		assertEquals(connection.isInDB("Person", "vorname", "Susi"), true);
	}

	@Test
	public void myqlinsertAbteilung() {
		connection.execUpdate("INSERT INTO Abteilung VALUES('bla',DEFAULT)");
		try {
			Thread.sleep(7000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertEquals(connection.isInDB("Abteilung", "aname", "bla"), true);
	}
	
	 @Test
	 public void mysqlinsertVeranstaltung() {
		 connection.execUpdate("INSERT INTO Veranstaltung VALUES('Super coole Party','2014-01-01',0,0,DEFAULT)");
		 try {
			 Thread.sleep(7000);
		 } catch (InterruptedException e) {
			 e.printStackTrace();
		 }
		 assertEquals(connection.isInDB("Veranstaltung", "vname","Super coole Party"),true);
		 assertEquals(pgconnection.isInDB("Veranstaltung", "vname","Super coole Party"),true);
	 }
	
	 @Test
	 public void mysqlinsertTeilnehmer() {
		 connection.execUpdate("INSERT INTO Teilnehmer VALUES('Hannah','Siegel','Vortrag Wichtig','2014-12-13',0,0,DEFAULT)");
		 try {
			 Thread.sleep(7000);
		 } catch (InterruptedException e) {
		 }
		 
		 assertEquals(connection.isInDB("Teilnehmer", "vorname", "Hannah"),true);
		 assertEquals(connection.isInDB("Teilnehmer", "vname","Vortrag Wichtig"),true);
		 assertEquals(pgconnection.isInDB("Besucher", "name","Hannah Siegel"),true);
	 }
	 
	 @Test
	 public void mysqlupdatePerson() {
		 connection.execUpdate("INSERT INTO Abteilung VALUES('Production',DEFAULT)");
		 connection.execUpdate("UPDATE PERSON SET aname='Production',sync_state='new' WHERE vorname='Gary'");
		 try {
			 Thread.sleep(12000);
		 } catch (InterruptedException e) {
		 }
		 assertEquals(connection.isInDB("Person", "aname", "Production"),true);
		 assertEquals(pgconnection.isInDB("Mitarbeiter", "abteilung",
		 "Production"),true);
	
	 }
	
	 @Test
	 public void postgresinsertePerson() {
	 
		 pgconnection.execUpdate("INSERT INTO Mitarbeiter VALUES('Vorname1 Mittelname1 Nachname1','IT',DEFAULT)");
		 try {
		 Thread.sleep(7000);
		 } catch (InterruptedException e) {
		 e.printStackTrace();
		 }
		 assertEquals(pgconnection.isInDB("Mitarbeiter", "name","Vorname1 Mittelname1 Nachname1"),true);
		 assertEquals(connection.isInDB("Person", "vorname", "Vorname1 Mittelname1"),true);
		 assertEquals(connection.isInDB("Person", "nachname", "Nachname1"),true);
	 }
//	
//	 @Test
//	 public void postgresdeletePerson() {
//	 
//		 pgconnection.execUpdate("DELETE FROM Mitarbeiter WHERE name ='Susi Mueller'");
//		 try {
//			 Thread.sleep(12000);
//		 } catch (InterruptedException e) {
//		 }
//		 assertEquals(pgconnection.isInDB("Mitarbeiter", "name",
//		 "Susi Mueller"),false);
//		 assertEquals(connection.isInDB("Person", "vorname", "Susi"),false);
//	 }
//	 @Test
//	 public void postgresdeleteBesucher() {
//		 pgconnection.execUpdate("DELETE FROM Besucher WHERE vname ='Party3'");
//		 try {
//			 Thread.sleep(12000);
//		 } catch (InterruptedException e) {
//		 }
//		 assertEquals(pgconnection.isInDB("Besucher", "vname","Party3"),false);
//		 assertEquals(connection.isInDB("Teilnehmer", "vname","Party3"),false);
//	 }
	 
	 @Test
	 public void postgresdeleteVeranstaltung() {
		 pgconnection.execUpdate("DELETE FROM Veranstaltung WHERE vname ='Party3'");
		 try {
			 Thread.sleep(12000);
		 } catch (InterruptedException e) {
		 }
		 assertEquals(pgconnection.isInDB("Veranstaltung", "vname", "Party3"),false);
		 assertEquals(connection.isInDB("Veranstaltung", "vname", "Party3"),false);
	 }
	 
	 @Test
	 public void postgresinserteVeranstaltung() {
		 pgconnection.execUpdate("INSERT INTO Veranstaltung VALUES('Party7','2015-11-11',true,50,DEFAULT)");
		 try {
			 Thread.sleep(7000);
		 } catch (InterruptedException e) {
		 }
		 assertEquals(pgconnection.isInDB("Veranstaltung", "vname", "Party7"),true);
		 assertEquals(connection.isInDB("Veranstaltung", "vname", "Party7"),true);
	 }
	
	 @Test
	 public void postgresinserteBesucher() {
		 pgconnection.execUpdate("INSERT INTO Besucher VALUES('Philip Schwarzkopf','Weihnachtsfeier',TO_DATE('23.12.2014', 'DD.MM.YYYY'),DEFAULT)");
		 try {
			 Thread.sleep(7000);
		 } catch (InterruptedException e) {
		 }
		 
		 assertEquals(connection.isInDB("Teilnehmer", "vorname", "Philip"),true);
		 assertEquals(connection.isInDB("Teilnehmer", "vname", "Weihnachtsfeier"),true);
		
		 assertEquals(pgconnection.isInDB("Besucher", "name","Philip Schwarzkopf"),true);
		 assertEquals(pgconnection.isInDB("Besucher", "vname","Weihnachtsfeier"),true);
	 }
}
