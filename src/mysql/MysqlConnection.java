package mysql;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import remoteInterfaces.SyncDBConnector;
import start.De;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSetMetaData;
import com.mysql.jdbc.Statement;

public class MysqlConnection implements SyncDBConnector {
	private Connection m_connection;
	
	public MysqlConnection(String db, String host, String user, String pw){
		try {
			com.mysql.jdbc.jdbc2.optional.MysqlDataSource d = new
			com.mysql.jdbc.jdbc2.optional.MysqlDataSource();
			d.setServerName(host);
			d.setDatabaseName(db);
			d.setUser(user);
			d.setPassword(pw);
			m_connection = (Connection) d.getConnection();
		}catch(Exception e){
			//TODO error handling
			System.out.println(e.getMessage());
		}
		De.bug("Succesfully connected to mysql db");
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
	
	public Connection getConnection(){
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
		De.bug("Mysql1 Executed Query : "+sql+"\n : "+ret);

		return ret;
	}
	
	public boolean execUpdate(String sql) {
		Statement st;
		boolean ret = true;
		try {
			st = (Statement) m_connection.createStatement();
			st.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			ret = false;
		}
		De.bug("Mysql1 Executed Query : "+sql+"\n : "+ret);

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
}
