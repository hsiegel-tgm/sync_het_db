package start;

import postgres.ChangeListenerPostgres;
import postgres.PostgresConnection;
import postgres.PostgresServer;
import syncserver.SyncServer;
import mysql.ChangeListenerMysql;
import mysql.MysqlConnection;
import mysql.MysqlServer;

public class Starter {
	public static void main(String arg[]){
		
		//I used:  mysql vsdb_03 localhost usr_vsdb03 pw_vsdb03
		if(arg.length < 6){
			System.out.println("The usage is the following: \n <mysql|postgres|testing|syncserver> <db> <host> <user> <password> <sync_server_ip> \n Please note, that the database should be set up correctly, as it can be found in our protocol. ");
		}
		else{
			String policy = "grant{permission java.security.AllPermission;};";
			System.setProperty("java.security.policy", policy.toString());
			
			String db_type = arg[0];
			String db = arg[1];
			String host = arg[2];
			String user = arg[3];
			String pw = arg[4];
			String syncserver_ip = arg[5];

			
			if(db_type.equals("mysql")){
				String name = "Mysql1";
				MysqlConnection mysql_connection = new MysqlConnection(db,host,user,pw,"Mysql1");
				MysqlServer srv = new MysqlServer(name,mysql_connection, syncserver_ip);
				ChangeListenerMysql clm = new ChangeListenerMysql(name,mysql_connection,syncserver_ip);
			}
			else if(db_type.equals("postgres")){
				String name = "Postgres1";
				PostgresConnection pg_connection = new PostgresConnection("vsdb_03","192.168.232.128","postgres","hermine11","Postgres1");
				PostgresServer srv = new PostgresServer(name,pg_connection,syncserver_ip);
				ChangeListenerPostgres clp = new ChangeListenerPostgres(name,pg_connection,syncserver_ip);
			}
			else if(db_type.equals("syncserver")){
				SyncServer a = new SyncServer();
			}
			else if(db_type.equals("testing")){
				SyncServer a = new SyncServer();
				// --------
				MysqlConnection connection = new MysqlConnection(db,host,user,pw,"Mysql1");
				MysqlServer srv = new MysqlServer("Mysql1",connection, syncserver_ip);
				// --------
				PostgresConnection pgconnection = new PostgresConnection("vsdb_03","192.168.232.128","postgres","hermine11","Postgres1");
				PostgresServer srv2 = new PostgresServer("Postgres1",pgconnection,syncserver_ip);
				// --------
				ChangeListenerMysql clm = new ChangeListenerMysql("Mysql1",connection,syncserver_ip);
				ChangeListenerPostgres clp = new ChangeListenerPostgres("Postgres1",pgconnection,syncserver_ip);
			}
			//TODO new auf yes aendern!
			else{
				System.out.println("The usage is the following: \n <mysql|postgres|testing|syncserver> <db> <host> <user> <password> <sync_server_ip> \n Please note, that the database should be set up correctly, as it can be found in our protocol. ");
			}
		}
	}
}
