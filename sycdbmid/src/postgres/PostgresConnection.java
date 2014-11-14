package postgres;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import remoteInterfaces.SyncDBConnector;
import start.De;

import java.sql.Statement;

/**
 * The class PostgresConnection is connection to the postgres database and has some of the important functions for inserting, updating...
 * 
 * @author Hannah Siegel
 * @version 2014-11-14
 *
 */
public class PostgresConnection implements SyncDBConnector{
	//Connection
	private java.sql.Connection m_connection;
	//name of the connection
	private String m_nameID;

	/**
	 * Constructor of the class, it is opening a new connection
	 * 
	 * @param db
	 * @param host
	 * @param user
	 * @param password
	 * @param name - name of the connection (for output reasons...)
	 */
	public PostgresConnection(String db, String host, String user, String password, String name) {
		m_nameID=name;
		try {
			Class.forName("org.postgresql.Driver");
			m_connection = (java.sql.Connection) DriverManager.getConnection("jdbc:postgresql://"+host+":5432/"+db, user,password);
			De.bug("Succesfully connected to " + m_nameID);
		} catch (ClassNotFoundException e) {
			System.out.println("The postgres Driver was not found");
		} catch (SQLException e) {
			System.out.println("The connection could not be fetched");
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
			System.out.println("There was an exception when deleting id:"+id+" from Logged on "+m_nameID);
		}
		return ret;
	}

	public java.sql.Connection getConnection() {
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
		System.out.println(m_nameID+" executed Query : "+sql+" result: "+ret);
		return ret;
	}
	
	/* (non-Javadoc)
	 * @see remoteInterfaces.SyncDBConnector#execUpdate(java.lang.String)
	 */
	public boolean execUpdate(String sql) {
		java.sql.Statement st;
		boolean ret = true;
		try {
			st = (java.sql.Statement) m_connection.createStatement();
			st.executeUpdate(sql);
			System.out.println(m_nameID+" executed Query : "+sql+" result: "+ret);
		} catch (SQLException e) {
			System.out.println("There was an exception when executing "+sql+" on "+m_nameID);
			ret = false;
		}
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
	
	/**
	 * 
	 */
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
