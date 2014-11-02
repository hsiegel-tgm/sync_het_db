package postgres;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;

import start.De;
import interfaces.Mapper;

public class PostgresServer implements Mapper {
	private static final long serialVersionUID = 1L;

	public PostgresServer(){
		String name = "Postgres";
		Mapper stub;
		try {
			stub = (Mapper) UnicastRemoteObject.exportObject(this, 0);
			Registry registry = LocateRegistry.getRegistry();
			//registry.bind(name, stub);
			De.bug("Postgres Server bound");
			
			//System.out.println("Press any key to unbound object");
			//System.in.read();
			//registry.unbind(name);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //catch (AlreadyBoundException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}
	}

	public boolean execute(int id, String action, String table, String pks,String values, Date date) throws RemoteException {
		return false; //TODO
	}
}
