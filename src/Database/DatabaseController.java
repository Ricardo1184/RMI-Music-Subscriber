package Database;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author Pajama Sammy
 */
public class DatabaseController
{

    private Registry registry = null;
    private IPersistencyMediator Database = null;
    private String currentIpAddress;
    private boolean connectedToDatabase;
    //private final String ipAddressDB = "localhost";
    private final String ipAddressDB = "192.168.1.35";

    private final int portNumber = 1088;
    private static final String bindingName = "Database";

    public IPersistencyMediator getDatabase()
    {
        return Database;
    }

    public boolean isConnectedToDatabase()
    {
        return connectedToDatabase;
    }

    public boolean connectToRMIDatabaseServer()
    {
        // Print IP address and port number for registry
        System.out.println("Client: IP Address: " + ipAddressDB);
        System.out.println("Client: Port number " + portNumber);

        // Locate registry at IP address and port number
        try
        {
            registry = LocateRegistry.getRegistry(ipAddressDB, portNumber);
        } catch (RemoteException ex)
        {
            System.out.println("Client: Cannot locate registry");
            System.out.println("Client: RemoteException: " + ex.getMessage());
            registry = null;
        }

        // Print result locating registry
        if (registry != null)
        {
            System.out.println("Client: Registry located");
        } else
        {
            System.out.println("Client: Cannot locate registry");
            System.out.println("Client: Registry is null pointer");
        }

        // Print contents of registry
        if (registry != null)
        {
            printContentsRegistry();
        }

        // Bind student administration using registry
        if (registry != null)
        {
            try
            {
                Database = (IPersistencyMediator) registry.lookup(bindingName);
                connectedToDatabase = true;
                System.out.println("Client: " + bindingName + " created");
            } catch (RemoteException ex)
            {
                System.out.println("Client: Cannot bind Database");
                System.out.println("Client: RemoteException: " + ex.getMessage());
                Database = null;
                connectedToDatabase = false;
            } catch (NotBoundException ex)
            {
                System.out.println("Client: Cannot bind Database");
                System.out.println("Client: NotBoundException: " + ex.getMessage());
                Database = null;
                connectedToDatabase = false;
            }
        }
        return connectedToDatabase;
    }

    // Print contents of registry
    private void printContentsRegistry()
    {
        try
        {
            String[] listOfNames = registry.list();
            System.out.println("Client: Database bound in registry:");
            if (listOfNames.length != 0)
            {
                for (String s : registry.list())
                {
                    System.out.println(s);
                }
            } else
            {
                System.out.println("Client: Database bound in registry is empty");
            }
        } catch (RemoteException ex)
        {
            System.out.println("Client: Cannot show Database bound in registry");
            System.out.println("Client: RemoteException: " + ex.getMessage());
        }
    }
}
