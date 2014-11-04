package mysql;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;

import remoteInterfaces.Mapper;
import start.De;

public class MysqlServer {
	Mapper stub;
	
	public MysqlServer(MysqlConnection connection){
		this.init(connection);
	}
	
	public void init(MysqlConnection connection) {
		String name = "Mysql";
		
		try {
			stub = (Mapper) UnicastRemoteObject.exportObject(new MysqlMapper(connection), 0);
			Registry registry = LocateRegistry.createRegistry(1099);
			registry.bind(name, stub);
			De.bug("Mysql Server bound");

		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
