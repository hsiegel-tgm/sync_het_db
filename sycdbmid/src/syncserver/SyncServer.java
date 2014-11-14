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
import java.util.Random;
import java.util.Vector;

import remoteInterfaces.Mapper;
import remoteInterfaces.MapperRegister;
import start.De;

/**
 * The class SyncServer is the implementation of the SyncServer which is porviding a rmi registry and which is handeling the synchronsiation
 * 
 * @author Hannah Siegel
 * @version 2014-11-11
 *
 */
public class SyncServer implements MapperRegister {
	private static final long serialVersionUID = 1L;
	//registered peers
	HashMap<String,Mapper> m_mapperObj = new HashMap<String,Mapper>();
	//Updates that have been succesful
	HashMap<String, Vector<String>> m_updates = new HashMap<String,Vector<String>>();

	/**
	 * Constructor of the class, creates the Rmi Registry and binds itself there
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

	/* (non-Javadoc)
	 * @see remoteInterfaces.Mapper#execute(java.lang.String, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.Date)
	 */
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
				 //getting key
				 String key = entry.getKey().toString();
				 
				 //if the operation has not yet been succesful or done
				 if(!(working_updates.contains(key))){
					 
					 //if the action is a update
					 if(action.equals("update")){
						 //Sending to all the peers (even the caller)
						Mapper mapperobj = (Mapper) entry.getValue();
						De.bug("sync server sending update: to "+key);
						ret =  mapperobj.execute(null, id, action,table,pks,values,date);
						if(ret == true){
							working_updates.addElement(key);
						}
					}else{
						//sending to all others than the cller
						if(!(key.equals(caller))){
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

		//if all the peers have been succesful: return true, else: return false
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
	public String register(String name, Mapper m) throws RemoteException {
		String real_key = name;
		while((m_mapperObj.containsKey(real_key))){
			real_key = name + new Random(100).nextInt();
		}
		m_mapperObj.put(real_key,m);
		return real_key;
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
