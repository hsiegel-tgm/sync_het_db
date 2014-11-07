package remoteInterfaces;

import java.rmi.RemoteException;
import java.rmi.Remote;
import java.util.Date;

public interface Mapper extends Remote {
	public boolean execute(String caller, int id, String action, String table, String pks, String values, Date date) throws RemoteException;
}
