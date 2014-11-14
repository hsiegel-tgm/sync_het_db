package remoteInterfaces;

import java.rmi.RemoteException;
import java.rmi.Remote;
import java.util.Date;

/**
 * The Mapper interface has the method execute, which is providing all the databases with a logger executing possibility
 * 
 * @author Hannah
 *
 */
public interface Mapper extends Remote {
	
	/**
	 * Excecute update/insert/delete
	 * 
	 * @param caller - nameID of the caller
	 * @param id - of the log in the DB
	 * @param action - insert/update/delete
	 * @param table - table on which the action has occured
	 * @param old_values - its old values (whenever deleting or updating)
	 * @param new_values - its new values (whenever inserting or updating)
	 * @param date 
	 * @return
	 * @throws RemoteException
	 */
	public boolean execute(String caller, int id, String action, String table, String old_values, String new_values, Date date) throws RemoteException;
}
