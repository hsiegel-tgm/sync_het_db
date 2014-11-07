package mysql;

import java.io.StringReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import remoteInterfaces.Mapper;
import start.De;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSetMetaData;
import com.mysql.jdbc.Statement;

public class ChangeListenerMysql implements Runnable {
	private MysqlConnection m_connectionobj;
	private Mapper m_mapper;
	
	public ChangeListenerMysql(MysqlConnection connection){
		m_connectionobj = connection;
		
		try{
			Registry registry = LocateRegistry.getRegistry("127.0.0.1",1099);
			m_mapper = (Mapper) registry.lookup("Postgres");
			this.run();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		while(true){
			int num = m_connectionobj.getLoggerCount();
			
			if(num > 0){
				ResultSet rs =m_connectionobj.getLoggerContent();
				try {
					//TODO implement something like processing and finished to handle exeptions!

					while(rs.next()){
						int id = rs.getInt(1); 
						String action = rs.getString(2); 
						String table = rs.getString(3); 
						String pks = rs.getString(4); 
						String values = rs.getString(5); 
						java.sql.Date date = rs.getDate(6); 

						boolean worked = m_mapper.execute(id, action,table, pks,values,date);
						
						if(worked){
							m_connectionobj.delteLogger(id);					
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (RemoteException e) {
					e.printStackTrace();
				} 
			}
			
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}