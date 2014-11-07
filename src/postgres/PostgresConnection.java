package postgres;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.postgresql.ds.PGPoolingDataSource;
import org.postgresql.*;

import start.De;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;


public class PostgresConnection {
	private Connection m_connection;

	public PostgresConnection(String db, String host, String user, String pw) {
		De.bug("Made Postgres conn");
		/*PGPoolingDataSource source = new PGPoolingDataSource();

		try {
			source.setDataSourceName("A Data Source");
			source.setServerName("192.168.232.128");
			source.setDatabaseName("vsdb_03");
			source.setUser("postgres");
			source.setPassword("hermine11");
			source.setMaxConnections(10);
			/*
			//TODO change datasource !!
			org.postgresql.ds.PGPoolingDataSource d = new org.postgresql.ds.PGPoolingDataSource();
			d.setServerName("192.168.232.128");
			d.setDatabaseName("vsdb_03");
			d.setUser("postgres");
			d.setPassword("hermine11");  
			//m_connection = (Connection) d.getConnection();
			
		} catch (Exception e) { 
			System.out.println(e.getMessage());
		}
		
		Connection conn = null;
		try {
		    conn = source.getConnection();
		    // use connection
		} catch (SQLException e) {
		    // log error
		} finally {
		    if (con != null) {
		        try { conn.close(); } catch (SQLException e) {}
		    }
		}*/
//		String url = "jdbc:postgresql://localhost/test";
//		Properties props = new Properties();
//		props.setProperty("user","fred");
//		props.setProperty("password","secret");
//		props.setProperty("ssl","true");
//		Connection conn = DriverManager.getConnection(url, props);
//		String url = "jdbc:postgresql://localhost/test?user=fred&password=secret&ssl=true";
//		Connection conn = DriverManager.getConnection(url);
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
