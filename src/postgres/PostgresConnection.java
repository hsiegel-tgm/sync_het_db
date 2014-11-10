package postgres;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import remoteInterfaces.SyncDBConnector;
import start.De;

import java.sql.Statement;

public class PostgresConnection implements SyncDBConnector{
	private java.sql.Connection m_connection;

	public PostgresConnection(String db, String host, String user, String pw) {

		try {
			 
			Class.forName("org.postgresql.Driver");
 
		} catch (ClassNotFoundException e) {
		}
		
		 m_connection = null;
		 
		try {
 
			m_connection = (java.sql.Connection) DriverManager.getConnection("jdbc:postgresql://192.168.232.128:5432/vsdb_03", "postgres","hermine11");
 
		} catch (SQLException e) {
		}

		De.bug("Succesfully connected to Postgres db");
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
		De.bug("PG-Executed Query : "+sql+"\n : "+ret);
		return ret;
	}
	
	public boolean execUpdate(String sql) {
		De.bug("running: ... "+sql);
		java.sql.Statement st;
		boolean ret = true;
		try {
			st = (java.sql.Statement) m_connection.createStatement();
			st.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			ret = false;
		}
		De.bug("PG-Executed Query : "+sql+"\n : "+ret);
		return ret;
	}
}
