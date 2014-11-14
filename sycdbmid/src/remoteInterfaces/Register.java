package remoteInterfaces;

import java.io.Serializable;
import java.rmi.RemoteException;

/**
 * The Interface Register is used so that the Server can register an Mapper Object.
 * 
 * @author Hannah Siegel
 * @version 2014-11-10
 */
public interface Register extends Serializable{
	
    /**
     * The method unregister is unregistering an Mapper Object
     * 
     * @param m Mapper Object
     * @throws RemoteException
     */
    public void unregister(String name, Mapper m) throws RemoteException;
    
    /**
     * The method register is registering an Mapper Object
     * 
     * @param m Mapper Object
     * @throws RemoteException
     */
    public String register(String name, Mapper m) throws RemoteException;

}
