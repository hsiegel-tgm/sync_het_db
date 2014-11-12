package postgres;

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
import javax.json.JsonObject;
import javax.json.JsonReader;

import remoteInterfaces.Mapper;
import start.De;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSetMetaData;
import com.mysql.jdbc.Statement;

public class ChangeListenerPostgres implements Runnable {
	private PostgresConnection m_connectionobj;
	private Mapper m_mapper;
	private String m_nameID;
	private Thread m_thread;
	
	public ChangeListenerPostgres(String nameID, PostgresConnection connection,String registryIp){
		m_connectionobj = connection;
		m_nameID = nameID;
		
		try{
			Registry registry = LocateRegistry.getRegistry(registryIp,1099);
			m_mapper = (Mapper) registry.lookup("SyncServer");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		m_thread = new Thread(this);
		m_thread.setName(nameID);
		m_thread.start();
	}
	
	public void run() {
		while(true){
			int num = m_connectionobj.getLoggerCount();
			
			if(num > 0){
				ResultSet rs =m_connectionobj.getLoggerContent();
				try {
					//TODO implement something like 'processing' and 'finished' to handle exeptions! LOST UPDATE and Transactions
					while(rs.next()){
						int id = rs.getInt(1); 
						String action = rs.getString(2); 
						String table = rs.getString(3); 
						String pks = rs.getString(4); 
						String values = rs.getString(5); 
						java.sql.Date date = rs.getDate(6); 

						boolean worked = m_mapper.execute(m_nameID,id, action, table, pks, values, date);
						
						if(worked){
							m_connectionobj.delteLogger(id);							
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (RemoteException e) {
					De.bug(e.getMessage());
					De.bug("------------------------");
					e.printStackTrace();
				} 
			}

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
