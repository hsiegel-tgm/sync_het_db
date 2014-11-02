package start;

import postgres.ChangeListenerPostgres;
import postgres.PostgresServer;
import mysql.ChangeListenerMysql;
import mysql.MysqlServer;

public class Starter {
	public static void main(String arg[]){
		try {
			Runtime.getRuntime().exec("rmiregistry");
			Runtime.getRuntime().exec("rmid -J-Djava.security.policy=rmid.policy");
		}
		catch (java.io.IOException e) {}

		//TODO: start rmireg :1099
		//TODO: Stop the vm networks
		
		//I used:  mysql vsdb_03 localhost usr_vsdb03 pw_vsdb03
		if(arg.length < 5){
			System.out.println("The usage is the following: \n <mysql|postgres> <db> <host> <user> <password> \n Please note, that the database should be set up correctly, as it can be found in our protocol. ");
		}
		else{
			String policy = "grant{permission java.security.AllPermission;};";
			System.setProperty("java.security.policy", policy.toString());
			
			String db_type = arg[0];
			String db = arg[1];
			String host = arg[2];
			String user = arg[3];
			String pw = arg[4];
			
			if(db_type.equals("mysql")){
				//new MysqlServer();
				new ChangeListenerMysql(db,host,user,pw);
			}
			else if(db_type.equals("postgres")){
				new PostgresServer();
				new ChangeListenerPostgres(db,host,user,pw);
			}
			else{
				System.out.println("The usage is the following: \n <mysql|postgres> <db> <host> <user> <password> \n Please note, that the database should be set up correctly, as it can be found in our protocol. ");
			}
		}
	}
}
