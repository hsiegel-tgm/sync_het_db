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
import remoteInterfaces.Register;
import start.De;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSetMetaData;
import com.mysql.jdbc.Statement;

/**
 * @author Hannah Siegel
 * @version 2014-11-14
 *
 */
public class ChangeListenerMysql implements Runnable {
	//Connection to the database
	private MysqlConnection m_connectionobj;
	//Mapper object (syncserver)
	private Mapper m_mapper;
	//Name
	private String m_nameID;
	//Thread
	private Thread m_thread;
	
	/**
	 * The Constructor of the class, it is fetching the object from the sync server
	 * 
	 * @param nameID
	 * @param connection
	 * @param syncServerIP
	 */
	public ChangeListenerMysql(String nameID, MysqlConnection connection, String syncServerIP){
		m_connectionobj = connection;
		m_nameID = nameID;
		try{
			Registry registry = LocateRegistry.getRegistry(syncServerIP,1099);
			m_mapper = (Mapper) registry.lookup("SyncServer");
		} catch (RemoteException e) {
			System.out.println("There was a remote error, could not find registry in "+m_nameID);
		} catch (NotBoundException e) {
			System.out.println("SyncServer not bound"+m_nameID);
		}
		
		//Threadding
		m_thread = new Thread(this);
		m_thread.setName(nameID);
		m_thread.start();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		while(true){
			//getting logger count
			int num = m_connectionobj.getLoggerCount();
			
			//if it is not null
			if(num > 0){
				//Getting the Logger content
				ResultSet rs =m_connectionobj.getLoggerContent();
				try {
					//Fetching every content
					while(rs.next()){
						//fetching values from row
						int id = rs.getInt(1); 
						String action = rs.getString(2); 
						String table = rs.getString(3); 
						String pks = rs.getString(4); 
						String values = rs.getString(5); 
						java.sql.Date date = rs.getDate(6); 

						//sending to sync server
						boolean worked = m_mapper.execute(m_nameID,id, action,table, pks,values,date);
						
						//if it worked : delete Log
						if(worked){
							m_connectionobj.delteLogger(id);					
						}
					}
				} catch (SQLException e) {
					System.out.println("There was an SQL Exception while fetching the Logger content");	
				} catch (RemoteException e) {
					System.out.println("There was a remote exception in "+m_nameID+" - executing command could not be send to sync server.");	
				} 
			}
			
			//Sleeping
			try {
				Thread.sleep(4500);
			} catch (InterruptedException e) {
			}
		}
	}

}
