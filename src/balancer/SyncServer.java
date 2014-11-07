package balancer;


import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Vector;

import remoteInterfaces.Mapper;
import start.De;

/**
 * The class Balancer is the implemantation of the Balancer
 * 
 * @author Hannah Siegel
 * @version 2013-12-14
 *
 */
public class SyncServer implements MapperRegister {

	private static final long serialVersionUID = 1L;

	// Vector<Mapper> m_MapperObjects = new Vector<Mapper>();
	
	HashMap<String,Mapper> m_mapperObj = new HashMap<String,Mapper>();
	
	/**
	 * Constructor
	 * Bounds the Balancer into the rmiregistry
	 * 
	 * Waits for key pressed, then unbounds.
	 */
	public SyncServer() {
		De.bug("Starting Sync Server...");
		String name = "SyncServer";
		MapperRegister x;
		try {

			//Exporting stub
			x = (MapperRegister) UnicastRemoteObject.exportObject(this, 0);
			
			//getting the registry
			Registry registry = LocateRegistry.createRegistry(1099);
			
			//binding Balancer
			registry.bind(name, x);
			De.bug("SyncServer bound");
			
			// System.out.println("Press any key to exit SyncServer!");
			// System.in.read();
			
			//unbinding Balancer
			// registry.unbind(name);
			// System.out.println("SyncServer unbound");

		} catch (Exception e) {
			//TODO
			e.printStackTrace();
		}
	}

	@Override
	public boolean execute(String caller, int id, String action, String table, String pks,String values, Date date) throws RemoteException {
		De.bug("executing in SyncServer... ");
		
		boolean ret = false;
		
		//TODO if they are down???
		//if there are any peers
		if (m_mapperObj == null || m_mapperObj.size() == 0) {
			De.bug("cannot execute... ");
			ret =  false;
		} else {
			ret = true;
			//TODO test if the 'false' thing works
			 for (Entry entry : m_mapperObj.entrySet()) {
				String key = entry.getKey().toString();
				if(!(key.equals(caller))){
					Mapper mapperobj = (Mapper) entry.getValue();
					ret =  mapperobj.execute(null, id, action,table,pks,values,date);
				}
				if (ret == false){
					break;
				}
			}
		}  
		return ret;
		//return false;
	}

	@Override
	public void unregister(String name, Mapper m) throws RemoteException {
		m_mapperObj.remove(name);
	}

	@Override
	public void register(String name, Mapper m) throws RemoteException {
		m_mapperObj.put(name,m);
	}
}
