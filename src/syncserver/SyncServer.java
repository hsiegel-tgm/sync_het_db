package syncserver;


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
import remoteInterfaces.MapperRegister;
import start.De;

/**
 * The class SyncServer is the implementation of the SyncServer
 * 
 * @author Hannah Siegel
 * @version 2014-11-11
 *
 */
public class SyncServer implements MapperRegister {
	private static final long serialVersionUID = 1L;
	
	HashMap<String,Mapper> m_mapperObj = new HashMap<String,Mapper>();
	HashMap<String, Vector<String>> m_updates = new HashMap<String,Vector<String>>();

	/**
	 * Constructor
	 * Bounds the Balancer into the rmiregistry
	 * 
	 * Waits for key pressed, then unbounds.
	 */
	public SyncServer() {
		String name = "SyncServer";
			
		MapperRegister x;
		//Exporting stub
		try {
			x = (MapperRegister) UnicastRemoteObject.exportObject(this, 0);
		
			//getting the registry
			Registry registry = LocateRegistry.createRegistry(1099);
			
			//binding Balancer
			registry.bind(name, x);
		} catch (AlreadyBoundException e) {
			System.out.println("Sync Server already bound, can not bound it again");
		} catch (RemoteException e) {
			System.out.println("There was a remote exception while exporting the Object");
		}
	}

	@Override
	public boolean execute(String caller, int id, String action, String table, String pks,String values, Date date) throws RemoteException {
		boolean ret = false;
		
		//add the logger to the que, if it not exists yet
		if(!(m_updates.containsKey(caller+id))){
			m_updates.put(caller+id, new Vector<String>());
		}
		
		//getting the vector
		Vector<String> working_updates = m_updates.get(caller+id);
		
		//if the action was not a update, adding the caller to the vector because he has already done the action
		if(!action.equals("update") && !working_updates.contains(caller) ){
			working_updates.addElement(caller);
		}
		
		if (m_mapperObj == null || m_mapperObj.size() == 0) {
			System.out.println("cannot execute... there are no registered peers!");
			ret =  false;
		} else {
			ret = true;
			 for (Entry entry : m_mapperObj.entrySet()) {
					String key = entry.getKey().toString();

					//De.bug("entry: "+entry.toString());
					//De.bug("key: "+key);
					De.bug("working updates: "+working_updates.toString());

					De.bug("y | n: "+!(working_updates.contains(key)));

				 if(!(working_updates.contains(key))){
					 De.bug("didnt work yet ...");
					 
					 if(action.equals("update")){
						De.bug("update! ..."+key);
						De.bug("working updates now: "+working_updates.toString());

						Mapper mapperobj = (Mapper) entry.getValue();
						De.bug("sync server sending update: to "+key);
						ret =  mapperobj.execute(null, id, action,table,pks,values,date);
						if(ret == true){
							De.bug("it worked! ... adding to the elemetns");
							working_updates.addElement(key);
						}
					}else{
						 De.bug("no update! ...");

						if(!(key.equals(caller))){
							De.bug("It is not the caller("+caller+")! ...it's "+key);

							Mapper mapperobj = (Mapper) entry.getValue();
							De.bug("sync server sending insert or delete: to "+key);
							ret =  mapperobj.execute(null, id, action,table,pks,values,date);
						}
						if (ret == true){
							working_updates.addElement(key);
						}
					}
				 }
			}
		}  
		
		m_updates.put(caller+id,working_updates);

		if(working_updates.size() == m_mapperObj.size()){
			De.bug((working_updates.size() ==  m_mapperObj.size())+"");
			return true;
		}else{
			return false;
		}
	}

	@Override
	public void unregister(String name, Mapper m) throws RemoteException {
		m_mapperObj.remove(name);
	}

	@Override
	public void register(String name, Mapper m) throws RemoteException {
		m_mapperObj.put(name,m);
	}
	
//
//	@Override
//	public boolean revertUpdate(int id) {
//		return false;
//	}
//
//	@Override
//	public boolean approveUpdate(int id) {
//		return false;
//	}
}
