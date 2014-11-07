package balancer;

import java.io.Serializable;
import java.rmi.RemoteException;

import remoteInterfaces.Mapper;

/**
 * The Interface Register is used so that the Server can register an Mapper Object.
 * 
 * @author Hannah Siegel
 * @version 2013-12-14
 */
public interface Register extends Serializable{
	
    /**
     * The method unregister is unregistering an Calculator Object
     * 
     * @param c Calculator Object
     * @throws RemoteException
     */
    public void unregister(String name, Mapper m) throws RemoteException;
    
    /**
     * The method register is registering an Calculator Object
     * 
     * @param c Calculator Object
     * @throws RemoteException
     */
    public void register(String name, Mapper m) throws RemoteException;

}
