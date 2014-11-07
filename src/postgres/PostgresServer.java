package postgres;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import balancer.MapperRegister;
import mysql.MysqlConnection;
import mysql.MysqlMapper;
import mysql.MysqlServer;
import remoteInterfaces.Mapper;
import start.De;

public class PostgresServer {
	private static final long serialVersionUID = 1L;
	
	Mapper stub;
	
	public PostgresServer(String name, PostgresConnection connection,String syncserverIP){
		this.init(name, connection, syncserverIP);
	}
	
	public void init(String name, PostgresConnection connection, String syncserverIP) {
		try {
			stub = (Mapper) UnicastRemoteObject.exportObject(new PostgresMapper(connection), 0);
			//Registry registry = LocateRegistry.createRegistry(1099);
			//registry.bind(name, stub);
			Registry registry = LocateRegistry.getRegistry(syncserverIP,1099);
			MapperRegister a = (MapperRegister) registry.lookup("SyncServer");
			a.register(name, stub);
			De.bug(name+" added to SyncServer");
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	
//	Mapper stub;
//		public PostgresServer(PostgresConnection connection){
//			this.init(connection);
//		}
//		
//		public void init(PostgresConnection connection) {
//			String name = "Postgres";
//			
//			try {
//				stub = (Mapper) UnicastRemoteObject.exportObject(new PostgresMapper(connection), 0);
//				Registry registry = LocateRegistry.createRegistry(1099);
//				registry.bind(name, stub);
//				De.bug("Postgres Server bound");
//				
//			} catch (RemoteException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (AlreadyBoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
}
