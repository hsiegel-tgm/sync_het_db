package start;

import balancer.SyncServer;
import postgres.ChangeListenerPostgres;
import postgres.PostgresConnection;
import postgres.PostgresServer;
import mysql.ChangeListenerMysql;
import mysql.MysqlConnection;
import mysql.MysqlServer;

public class Starter {
	public static void main(String arg[]){
		//try {
		//	Runtime.getRuntime().exec("rmiregistry");
		//	Runtime.getRuntime().exec("rmid -J-Djava.security.policy=rmid.policy");
		//}
		//catch (java.io.IOException e) {}

		//TODO: start rmireg :1099
		//TODO: Stop the vm networks
		
		//I used:  mysql vsdb_03 localhost usr_vsdb03 pw_vsdb03
		if(arg.length < 5){
			System.out.println("The usage is the following: \n <mysql|postgres> <db> <host> <user> <password> <registry-ip> \n Please note, that the database should be set up correctly, as it can be found in our protocol. ");
		}
		else{
			String policy = "grant{permission java.security.AllPermission;};";
			System.setProperty("java.security.policy", policy.toString());
			
			String db_type = arg[0];
			String db = arg[1];
			String host = arg[2];
			String user = arg[3];
			String pw = arg[4];
			String regip = arg[5];

			
			if(db_type.equals("mysql")){
				String name = "Mysql1";
				MysqlConnection connection = new MysqlConnection(db,host,user,pw);
				MysqlServer srv = new MysqlServer(name,connection, regip);
				ChangeListenerMysql clm = new ChangeListenerMysql(name,connection,regip);
			}
			else if(db_type.equals("postgres")){
				String name = "Postgres1";
				PostgresConnection connection = new PostgresConnection(db,host,user,pw);
				PostgresServer srv = new PostgresServer(name,connection,regip);
				ChangeListenerPostgres clp = new ChangeListenerPostgres(name,connection,regip);
			}
			else if(db_type.equals("syncserver")){
				SyncServer a = new SyncServer();
			}
			else if(db_type.equals("pgcontest")){
				PostgresConnection connection = new PostgresConnection(db,host,user,pw);
				connection.getLoggerCount(); //TODO Testen
				connection.getLoggerContent(); //TODO Testen
			}
			else if(db_type.equals("testing")){
				SyncServer a = new SyncServer();
				// --------
				MysqlConnection connection = new MysqlConnection(db,host,user,pw);
				MysqlServer srv = new MysqlServer("Mysql1",connection, regip);
				// --------
				PostgresConnection connection2 = new PostgresConnection(db,host,user,pw);
				PostgresServer srv2 = new PostgresServer("Postgres1",connection2,regip);
				ChangeListenerMysql clm = new ChangeListenerMysql("Mysql1",connection,regip);
				//ChangeListenerPostgres clp = new ChangeListenerPostgres("Postgres1",connection2,regip);
				//clp.run();
				//clm.run();
			}
		
			else{
				System.out.println("The usage is the following: \n <mysql|postgres|testing|syncserver> <db> <host> <user> <password> \n Please note, that the database should be set up correctly, as it can be found in our protocol. ");
			}
		}
	}
}
