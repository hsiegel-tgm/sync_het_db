package remoteInterfaces;

import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.util.Date;


public interface SyncDBConnector {
	/**
	 * The method get Logger Count is returning the current number of data in the logger
	 * 
	 * @return number of entries
	 */
	public int getLoggerCount();
	
	/**
	 * The method get Logger Content is returning a result set of the logger's content, in order to do some processing
	 * 
	 * @return number of entries
	 */
	public ResultSet getLoggerContent();
	
	/**
	 * The method delete Logger  is deletign a logger if everything has worked
	 *
	 * @param id which logger to delete
	 * @return
	 */
	public int delteLogger(int id);
	
	/**
	 * The method execQuery is excecuting a query to the database
	 * 
	 * @param sql_string
	 * @return true if there was no error, else otherwise
	 */
	public boolean execQuery(String sql_string);
	
	/**
	 * The method execUpdate is excecuting a query to the database
	 * 
	 * @param sql_string
	 * @return true if there was no error, else otherwise
	 */
	public boolean execUpdate(String sql_string);
	

	/**
	 *  The method isInDB is checking if a certain value is in a certain attribute in a certain table
	 * 
	 * @param table
	 * @param attribute
	 * @param value
	 * @return true if the value is in the database, false otherwise
	 */
	public boolean isInDB(String table, String attribute, String value);

}
