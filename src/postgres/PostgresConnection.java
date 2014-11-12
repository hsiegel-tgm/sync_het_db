package postgres;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import remoteInterfaces.SyncDBConnector;
import start.De;

import java.sql.Statement;

public class PostgresConnection implements SyncDBConnector{
	private java.sql.Connection m_connection;
	private String m_nameID;

	public PostgresConnection(String db, String host, String user, String pw, String name) {
		m_nameID=name;
		try {
			Class.forName("org.postgresql.Driver");
			//m_connection = (java.sql.Connection) DriverManager.getConnection("jdbc:postgresql://192.168.232.128:5432/vsdb_03", "postgres","hermine11");
			m_connection = (java.sql.Connection) DriverManager.getConnection("jdbc:postgresql://"+host+":5432/"+db, user,pw);
			De.bug("Succesfully connected to " + m_nameID);
		} catch (ClassNotFoundException e) {
			System.out.println("The postgres Driver was not found");
		} catch (SQLException e) {
			System.out.println("The connection could not be fetched");
		}
	}

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
			e.printStackTrace();
		}
		return rows;
	}

	public ResultSet getLoggerContent() {
		Statement st;
		ResultSet rs = null;
		try {
			st = (Statement) m_connection.createStatement();
			rs = st.executeQuery("SELECT * FROM logged");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	public int delteLogger(int id) {
		Statement st;
		int ret = 0;
		try {
			st = (Statement) m_connection.createStatement();
			ret = st.executeUpdate("DELETE FROM logged WHERE id="+id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public java.sql.Connection getConnection() {
		return m_connection;
	}
	
	public boolean execQuery(String sql) {
		Statement st;
		boolean ret = true;
		try {
			st = (Statement) m_connection.createStatement();
			st.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			ret = false;
		}
		System.out.println(m_nameID+" executed Query : "+sql+" result: "+ret);
		return ret;
	}
	
	public boolean execUpdate(String sql) {
		java.sql.Statement st;
		boolean ret = true;
		try {
			st = (java.sql.Statement) m_connection.createStatement();
			st.executeUpdate(sql);
			System.out.println(m_nameID+" executed Query : "+sql+" result: "+ret);
		} catch (SQLException e) {
			e.printStackTrace();
			ret = false;
		}
		return ret;
	}

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
			e.printStackTrace();
		}
		return false;
	}
	
	public void close(){
		try {
			m_connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
