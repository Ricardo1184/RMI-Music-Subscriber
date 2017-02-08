package Database;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * @author Pajama Sammy
 */
public interface IPersistencyMediator extends Remote
{
    
    public void AddUser(String userName) throws RemoteException;
    public boolean LogIn(String userName) throws RemoteException;
    public boolean AddArtist(String Artist) throws RemoteException;
    public ArrayList<String> getArtists(String artistName) throws RemoteException;
    public boolean AddArtistSubscription(String userName, String artist) throws RemoteException;
    public boolean RemoveArtistSubscription(String userName, String artist) throws RemoteException;
    public boolean getArtistSubscription(String userName, String artist) throws RemoteException;
}
