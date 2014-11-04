package postgres;

import java.io.StringReader;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import mysql.MysqlServer;
import remoteInterfaces.Mapper;
import start.De;

public class PostgresServer {
	private static final long serialVersionUID = 1L;
	Mapper stub;
		public PostgresServer(PostgresConnection connection){
			this.init(connection);
		}
		
		public void init(PostgresConnection connection) {
			String name = "Postgres";
			
			try {
				stub = (Mapper) UnicastRemoteObject.exportObject(new PostgresMapper(connection), 0);
				Registry registry = LocateRegistry.createRegistry(1099);
				registry.bind(name, stub);
				De.bug("Postgres Server bound");
				
				//System.out.println("Press any key to unbound object");
				//System.in.read();
				//registry.unbind(name);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (AlreadyBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
}
