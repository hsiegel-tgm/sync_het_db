package postgres;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class PostgresConnection {
	private Connection m_connection;

	public PostgresConnection(String db, String host, String user, String pw) {
		try {
			//TODO change datasource !!
			com.mysql.jdbc.jdbc2.optional.MysqlDataSource d = new com.mysql.jdbc.jdbc2.optional.MysqlDataSource();
			d.setServerName(host);
			d.setDatabaseName(db);
			d.setUser(user);
			d.setPassword(pw);
			m_connection = (Connection) d.getConnection();
		} catch (Exception e) { 
			System.out.println(e.getMessage());
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
			ret = st.executeUpdate("DELETE FROM logger WHERE id="+id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public Connection getConnection() {
		return m_connection;
	}
}
