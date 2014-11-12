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
		MysqlServer srv = new MysqlServer("Mysql1",connection, "172.0.0.1");
		// --------
		PostgresConnection connection2 = new PostgresConnection("","","","","Postgres1");
		PostgresServer srv2 = new PostgresServer("Postgres1",connection2,"127.0.0.1");
		// --------
		ChangeListenerMysql clm = new ChangeListenerMysql("Mysql1",connection,"127.0.0.1");
		ChangeListenerPostgres clp = new ChangeListenerPostgres("Postgres1",connection2,"127.0.0.1");	
		connection2.execUpdate("UPDATE PERSON SET aname='Abteilung2',sync_state='new' WHERE vorname='Paul'");
		connection2.close();
		connection.close();

		//SyncServer tester = new SyncServer();
		//SyncServer tester2 = new SyncServer();

	    //assertEquals("Sync Server already bound, can not bound it again", outContent.toString());
	}

	
}
