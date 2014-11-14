package test;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import mysql.ChangeListenerMysql;
import mysql.MysqlConnection;
import mysql.MysqlServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import postgres.ChangeListenerPostgres;
import postgres.PostgresConnection;
import postgres.PostgresServer;
import syncserver.SyncServer;

public class SyncServerTest {
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

	@Before
	public void setUpStreams() {
	    System.setOut(new PrintStream(outContent));
	    System.setErr(new PrintStream(errContent));
	}

	@After
	public void cleanUpStreams() {
	    System.setOut(null);
	    System.setErr(null);
	}
	
	@Test
	public void normalStartup() {
	   SyncServer tester = new SyncServer();
	} 
	
//	@Test
//	public void alreadyBound() {
//		//SyncServer tester = new SyncServer();
//		//SyncServer tester2 = new SyncServer();
//
//	    //assertEquals("Sync Server already bound, can not bound it again", outContent.toString());
//	}

	@Test
	public void sycServer() {
		SyncServer a = new SyncServer();
		
		
		MysqlConnection connection = new MysqlConnection("vsdb_03","localhost","usr_vsdb03","pw_vsdb03","Mysql1");
		MysqlServer srv = new MysqlServer("Mysql1",connection, "127.0.0.1");
		// --------
		PostgresConnection pgconnection = new PostgresConnection("vsdb_03","192.168.232.128","postgres","hermine11","Postgres1");
		PostgresServer srv2 = new PostgresServer("Postgres1",pgconnection,"127.0.0.1");
		// --------
		ChangeListenerMysql clm = new ChangeListenerMysql("Mysql1",connection,"127.0.0.1");
		ChangeListenerPostgres clp = new ChangeListenerPostgres("Postgres1",pgconnection,"127.0.0.1");	
		pgconnection.execUpdate("DELETE FROM Besucher WHERE name='Paul Adeyemi'");
		pgconnection.close();
		connection.close();
	}
}
