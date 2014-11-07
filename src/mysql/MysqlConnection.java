package mysql;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import start.De;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSetMetaData;
import com.mysql.jdbc.Statement;

public class MysqlConnection {
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
		De.bug("mysql connection worked");
	}
	
//	/**
//	 * only for debugging
//	 * 
//	 * @param query
//	 */
//	public void printExecQuery(String query){
//		Statement st;
//		try {
//			st = (Statement) m_connection.createStatement();
//			ResultSet rs = st.executeQuery(query);
//			ResultSetMetaData x = (ResultSetMetaData) rs.getMetaData();
//			
//			for(int i = 1; i <= x.getColumnCount(); ++i){
//				De.bug((x.getColumnName(i)) + " | ");
//			}
//			De.bug("\n ------------------- ");
//			while (rs.next()) {
//				for(int i = 1; i <= x.getColumnCount(); ++i){
//					De.bug(rs.getString(x.getColumnName(i)) + " | ");
//				}
//				De.bug("\n ------------------- ");
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public int countExecQuery(String query){
//		Statement st;
//		int rows = 0;
//		try {
//			st = (Statement) m_connection.createStatement();
//			ResultSet rs = st.executeQuery(query);
//			while (rs.next()) {
//				rows++;
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return rows;
//	}
//	
//	public ResultSet RSExecQuery(String query){
//		
//		Statement st;
//		ResultSet rs=null;
//		try {
//			st = (Statement) m_connection.createStatement();
//			rs = st.executeQuery(query);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return rs;
//	}
//	
//	public int RSExecUpdate(String query){
//		Statement st;
//		int ret=0;
//		try {
//			st = (Statement) m_connection.createStatement();
//			ret = st.executeUpdate(query);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return ret;
//	}
	
	
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
	
	public boolean execCommand(String sql) {
		Statement st;
		boolean ret = false;
		try {
			st = (Statement) m_connection.createStatement();
			st.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			ret = false;
		}
		return ret;
	}
}
