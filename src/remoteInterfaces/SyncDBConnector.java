package remoteInterfaces;

import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.util.Date;


public interface SyncDBConnector {
	public int getLoggerCount();
	public ResultSet getLoggerContent();
	public int delteLogger(int id);
	public boolean execQuery(String sql);
	public boolean execUpdate(String sql);
}
