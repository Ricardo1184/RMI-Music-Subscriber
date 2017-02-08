package MusicSubscriberServer;

import Database.DatabaseServer;
import fontyspublisher.RemotePublisher;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author Pajama Sammy
 */
public class StartServer
{

    private static int portNumber = 1099;
    private static String bindingName = "publisher";

    /**
     * @param args the command line arguments
     * @throws java.rmi.RemoteException
     */
    public static void main(String[] args) throws RemoteException
    {
        // Create an instance of RemotePublisher
        RemotePublisher remotePublisher = new RemotePublisher();

        // Create registry and register remote publisher
        Registry registry = LocateRegistry.createRegistry(portNumber);
        registry.rebind(bindingName, remotePublisher);

        DatabaseServer d  = new DatabaseServer();
        // Remote publisher registered
        System.out.println("Remote publisher registered.");
        System.out.println("Port number  : " + portNumber);
        System.out.println("Binding name : " + bindingName);
    }
}
