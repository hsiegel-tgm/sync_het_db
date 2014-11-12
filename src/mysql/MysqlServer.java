package mysql;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;

import remoteInterfaces.Mapper;
import remoteInterfaces.MapperRegister;
import start.De;
import syncserver.SyncServer;

public class MysqlServer {
	Mapper stub;
	
	public MysqlServer(String name, MysqlConnection connection,String syncserverIP){
		this.init(name, connection, syncserverIP);
	}
	
	public void init(String name, MysqlConnection connection, String syncserverIP) {
		
		try {
			stub = (Mapper) UnicastRemoteObject.exportObject(new MysqlMapper(connection), 0);
			//Registry registry = LocateRegistry.createRegistry(1099);
			//registry.bind(name, stub);
			Registry registry = LocateRegistry.getRegistry(syncserverIP,1099);
			MapperRegister a = (MapperRegister) registry.lookup("SyncServer");
			a.register(name, stub);
			De.bug("Mysql1 added to SyncServer");
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
