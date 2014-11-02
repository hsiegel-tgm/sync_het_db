package interfaces;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Date;

public interface Mapper extends java.rmi.Remote, Serializable {
	public boolean execute(int id, String action, String table, String pks, String values, Date date) throws RemoteException;
}
