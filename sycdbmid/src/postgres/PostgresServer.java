package postgres;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import remoteInterfaces.Mapper;
import remoteInterfaces.MapperRegister;
import start.De;

/**
 * @author Hannah Siegel
 * @version 2014-11-13
 *
 */
public class PostgresServer {
	Mapper m_stub;
	
	/**
	 * Constructor of the class
	 * Initializes the registration
	 * 
	 * @param name
	 * @param connection
	 * @param syncserverIP
	 */
	public PostgresServer(String name, PostgresConnection connection,String syncserverIP){
		this.init(name, connection, syncserverIP);
	}
	
	public void init(String name, PostgresConnection connection, String syncserverIP) {
		try {
			//Stub object
			m_stub = (Mapper) UnicastRemoteObject.exportObject( new PostgresMapper(connection), 0);
			
			//Get Registry
			Registry registry = LocateRegistry.getRegistry(syncserverIP,1099);
			
			//Lookup the SyncServr
			MapperRegister a = (MapperRegister) registry.lookup("SyncServer");
			
			//register the stub
			String new_name = a.register(name, m_stub);
			connection.setNameID(new_name);
			De.bug("Mysql1 added to SyncServer");
			
			
		} catch (RemoteException e) {
			System.out.println("There was a remote exception while trying to fetch registry");
		} catch (NotBoundException e) {
			System.out.println("Sync Server not bound");
		} 
	}
}
