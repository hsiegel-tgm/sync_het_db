package mysql;

import java.sql.ResultSet;
import java.sql.SQLException;

import remoteInterfaces.SyncDBConnector;
import start.De;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

/**
 * The class MysqlConnection is connection to the mysql database and has some of the important functions for inserting, updating...
 * 
 * @author Hannah Siegel
 * @version 2014-11-12
 *
 */
public class MysqlConnection implements SyncDBConnector {
	//Connection to the DB
	private Connection m_connection;
	
	//name of the connection
	private String m_nameID;
	
	/**
	 * The constructor of the class, starts the connection
	 * 
	 * @param db
	 * @param host
	 * @param user
	 * @param pw
	 * @param name - name of the connection
	 */
	public MysqlConnection(String db, String host, String user, String pw,String name){
		m_nameID = name;
		
		try {
			com.mysql.jdbc.jdbc2.optional.MysqlDataSource d = new
			com.mysql.jdbc.jdbc2.optional.MysqlDataSource();
			d.setServerName(host);
			d.setDatabaseName(db);
			d.setUser(user);
			d.setPassword(pw);
			m_connection = (Connection) d.getConnection();
			De.bug("Succesfully connected to "+name);
		}catch(Exception e){
			System.out.println("Connection to "+name+" DB was not possible: "+e.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see remoteInterfaces.SyncDBConnector#getLoggerCount()
	 */
	public int getLoggerCount() {
		Statement st;
		int rows = 0;
		try {
			st = (Statement) m_connection.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM logged");
			while (rs.next()) {
				rows++;
			}
		} catch (SQLException e) {
			System.out.println("There was an exception when fetching from logged on "+m_nameID);
		}
		return rows;
	}

	/* (non-Javadoc)
	 * @see remoteInterfaces.SyncDBConnector#getLoggerContent()
	 */
	public ResultSet getLoggerContent() {
		Statement st;
		ResultSet rs = null;
		try {
			st = (Statement) m_connection.createStatement();
			rs = st.executeQuery("SELECT * FROM logged");
		} catch (SQLException e) {
			System.out.println("There was an exception when fetching from logged on "+m_nameID);
		}
		return rs;
	}

	/* (non-Javadoc)
	 * @see remoteInterfaces.SyncDBConnector#delteLogger(int)
	 */
	public int delteLogger(int id) {
		Statement st;
		int ret = 0;
		try {
			st = (Statement) m_connection.createStatement();
			ret = st.executeUpdate("DELETE FROM logged WHERE id="+id);
		} catch (SQLException e) {
			System.out.println("There was an exception when deleting from logged on "+m_nameID);
		}
		return ret;
	}
	
	/**
	 * @return Connection
	 */
	public Connection getConnection(){
		return m_connection;
	}
	
	/* (non-Javadoc)
	 * @see remoteInterfaces.SyncDBConnector#execQuery(java.lang.String)
	 */
	public boolean execQuery(String sql) {
		Statement st;
		boolean ret = true;
		try {
			st = (Statement) m_connection.createStatement();
			st.executeQuery(sql);
		} catch (SQLException e) {
			System.out.println("There was an exception when executing "+sql+" on "+m_nameID);
			ret = false;
		}
		System.out.println(m_nameID+" Executed Query : "+sql+" : "+ret);

		return ret;
	}
	
	/* (non-Javadoc)
	 * @see remoteInterfaces.SyncDBConnector#execUpdate(java.lang.String)
	 */
	public boolean execUpdate(String sql) {
		Statement st;
		boolean ret = true;
		try {
			st = (Statement) m_connection.createStatement();
			st.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println("There was an exception when executing "+sql+" on "+m_nameID);
			ret = false;
		}
		System.out.println(m_nameID+" Executed Query : "+sql+" : "+ret);

		return ret;
	}

	/* (non-Javadoc)
	 * @see remoteInterfaces.SyncDBConnector#isInDB(java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean isInDB(String table, String attribute, String value) {
		Statement st;
		ResultSet rs = null;
		try {
			st = (Statement) m_connection.createStatement();
			rs = st.executeQuery("SELECT " +attribute+" FROM "+table);
			while(rs.next()){
				if(rs.getString(1).equals(value)){
					return true;
				}
			}
		} catch (SQLException e) {
			System.out.println("There was a sql exception when tring to fetch if "+value+" is in"+table+" - "+attribute);
		}
		return false;
	}
	
	public void close(){
		try {
			m_connection.close();
		} catch (SQLException e) {
			System.out.println("Could not close the connection");
		}
	}
	

	public void setNameID(String new_name){
		m_nameID = new_name;
	}
}
