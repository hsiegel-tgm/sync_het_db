package test;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
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
	public void setUpStreams() {
	}

	@After
	public void cleanUpStreams() {
	//	connection.close();
	//	pgconnection.close();
	}
	
//	@Test
//	public void myqlinsertPerson() {
//		SyncServer a = new SyncServer();
//		// --------
//		//I used:  mysql vsdb_03 localhost usr_vsdb03 pw_vsdb03
//
//		connection = new MysqlConnection("vsdb_03","localhost","usr_vsdb03","pw_vsdb03","Mysql1");
//		MysqlServer srv = new MysqlServer("Mysql1",connection, "127.0.0.1");
//		// --------
//		pgconnection = new PostgresConnection("vsdb_03","192.168.232.128","postgres","hermine11","Postgres1");
//		PostgresServer srv2 = new PostgresServer("Postgres1",pgconnection,"127.0.0.1");
//		// --------
//		ChangeListenerMysql clm = new ChangeListenerMysql("Mysql1",connection,"127.0.0.1");
//		ChangeListenerPostgres clp = new ChangeListenerPostgres("Postgres1",pgconnection,"127.0.0.1");	
//		connection.execUpdate("INSERT INTO Person VALUES('Susi','Mueller','HR','addresse wien',DEFAULT)");
//		try {
//			Thread.sleep(7000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		assertEquals(pgconnection.isInDB("Mitarbeiter", "name", "Susi Mueller"),true);
//	} 
//	
//	@Test
//	public void mysqlinsertVeranstaltung() {
//		SyncServer a = new SyncServer();
//		// --------
//		//I used:  mysql vsdb_03 localhost usr_vsdb03 pw_vsdb03
//
//		connection = new MysqlConnection("vsdb_03","localhost","usr_vsdb03","pw_vsdb03","Mysql1");
//		MysqlServer srv = new MysqlServer("Mysql1",connection, "127.0.0.1");
//		// --------
//		pgconnection = new PostgresConnection("vsdb_03","192.168.232.128","postgres","hermine11","Postgres1");
//		PostgresServer srv2 = new PostgresServer("Postgres1",pgconnection,"127.0.0.1");
//		// --------
//		ChangeListenerMysql clm = new ChangeListenerMysql("Mysql1",connection,"127.0.0.1");
//		ChangeListenerPostgres clp = new ChangeListenerPostgres("Postgres1",pgconnection,"127.0.0.1");	
//		connection.execUpdate("INSERT INTO Veranstaltung VALUES('Super coole Party','2014-01-01',0,0,DEFAULT)");
//		try {
//			Thread.sleep(7000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		assertEquals(connection.isInDB("Veranstaltung", "vname", "Super coole Party"),true);
//		assertEquals(pgconnection.isInDB("Veranstaltung", "vname", "Super coole Party"),true);
//
//	} 
//	
//	@Test
//	public void mysqlinsertTeilnehmer() {
//		SyncServer a = new SyncServer();
//		// --------
//		//I used:  mysql vsdb_03 localhost usr_vsdb03 pw_vsdb03
//
//		connection = new MysqlConnection("vsdb_03","localhost","usr_vsdb03","pw_vsdb03","Mysql1");
//		MysqlServer srv = new MysqlServer("Mysql1",connection, "127.0.0.1");
//		// --------
//		pgconnection = new PostgresConnection("vsdb_03","192.168.232.128","postgres","hermine11","Postgres1");
//		PostgresServer srv2 = new PostgresServer("Postgres1",pgconnection,"127.0.0.1");
//		// --------
//		ChangeListenerMysql clm = new ChangeListenerMysql("Mysql1",connection,"127.0.0.1");
//		ChangeListenerPostgres clp = new ChangeListenerPostgres("Postgres1",pgconnection,"127.0.0.1");	
//		connection.execUpdate("INSERT INTO Teilnehmer VALUES('Hannah','Siegel','Vortrag Wichtig','2014-12-13',0,0,DEFAULT)");
//		try {
//			Thread.sleep(7000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		assertEquals(connection.isInDB("Teilnehmer", "vorname", "Hannah"),true);
//		assertEquals(connection.isInDB("Teilnehmer", "vname", "Vortrag Wichtig"),true);
//
//		assertEquals(pgconnection.isInDB("Besucher", "name", "Hannah Siegel"),true);
//		//assertEquals(pgconnection.isInDB("Veranstaltung", "verpflichtend", "false"),true);
//
//	} 
	@Test
	public void mysqlupdatePerson() {
		SyncServer a = new SyncServer();
		// --------
		//I used:  mysql vsdb_03 localhost usr_vsdb03 pw_vsdb03

		connection = new MysqlConnection("vsdb_03","localhost","usr_vsdb03","pw_vsdb03","Mysql1");
		MysqlServer srv = new MysqlServer("Mysql1",connection, "127.0.0.1");
		// --------
		pgconnection = new PostgresConnection("vsdb_03","192.168.232.128","postgres","hermine11","Postgres1");
		PostgresServer srv2 = new PostgresServer("Postgres1",pgconnection,"127.0.0.1");
		// --------
		ChangeListenerMysql clm = new ChangeListenerMysql("Mysql1",connection,"127.0.0.1");
		ChangeListenerPostgres clp = new ChangeListenerPostgres("Postgres1",pgconnection,"127.0.0.1");	
		connection.execUpdate("INSERT INTO Abteilung VALUES('Production',DEFAULT)");
		connection.execUpdate("UPDATE PERSON SET aname='Production',sync_state='new' WHERE vorname='Gary'");
		try {
			Thread.sleep(7000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(connection.isInDB("Person", "aname", "Production"),true);
		assertEquals(pgconnection.isInDB("Mitarbeiter", "abteilung", "Production"),true);

	} 
	
	@Test
	public void postgresinsertePerson() {
		SyncServer a = new SyncServer();
		// --------
		//I used:  mysql vsdb_03 localhost usr_vsdb03 pw_vsdb03

		connection = new MysqlConnection("vsdb_03","localhost","usr_vsdb03","pw_vsdb03","Mysql1");
		MysqlServer srv = new MysqlServer("Mysql1",connection, "127.0.0.1");
		// --------
		pgconnection = new PostgresConnection("vsdb_03","192.168.232.128","postgres","hermine11","Postgres1");
		PostgresServer srv2 = new PostgresServer("Postgres1",pgconnection,"127.0.0.1");
		// --------
		ChangeListenerMysql clm = new ChangeListenerMysql("Mysql1",connection,"127.0.0.1");
		ChangeListenerPostgres clp = new ChangeListenerPostgres("Postgres1",pgconnection,"127.0.0.1");	
		pgconnection.execUpdate("INSERT INTO Mitarbeiter VALUES('Vorname1 Mittelname1 Nachname1','IT',DEFAULT)");
		try {
			Thread.sleep(7000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(pgconnection.isInDB("Mitarbeiter", "name", "Vorname1 Mittelname1 Nachname1"),true);
		assertEquals(connection.isInDB("Person", "vorname", "Vorname1 Mittelname1"),true);
		assertEquals(connection.isInDB("Person", "nachname", "Nachname1"),true);
		//assertEquals(pgconnection.isInDB("Veranstaltung", "verpflichtend", "false"),true);
	} 
//	
//	@Test
//	public void postgresupdatePerson() {
//		SyncServer a = new SyncServer();
//		// --------
//		//I used:  mysql vsdb_03 localhost usr_vsdb03 pw_vsdb03
//
//		connection = new MysqlConnection("vsdb_03","localhost","usr_vsdb03","pw_vsdb03","Mysql1");
//		MysqlServer srv = new MysqlServer("Mysql1",connection, "127.0.0.1");
//		// --------
//		 pgconnection = new PostgresConnection("vsdb_03","192.168.232.128","postgres","hermine11","Postgres1");
//		PostgresServer srv2 = new PostgresServer("Postgres1",pgconnection,"127.0.0.1");
//		// --------
//		ChangeListenerMysql clm = new ChangeListenerMysql("Mysql1",connection,"127.0.0.1");
//		ChangeListenerPostgres clp = new ChangeListenerPostgres("Postgres1",pgconnection,"127.0.0.1");	
//		pgconnection.execUpdate("UPDATE Mitarbeiter SET abteilung='Abteilung2',sync='new' WHERE name='Paul'");
//		try {
//			Thread.sleep(7000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		assertEquals(connection.isInDB("Person", "aname", "Abteilung2"),true);
//		assertEquals(connection.isInDB("Abteilung", "aname", "Abteilung2"),true);
//
//		assertEquals(pgconnection.isInDB("Mitarbeiter", "abteilung", "Abteilung2"),true);
//		//assertEquals(pgconnection.isInDB("Veranstaltung", "verpflichtend", "false"),true);
//	} 
}
