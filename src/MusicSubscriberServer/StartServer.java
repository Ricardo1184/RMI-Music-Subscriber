package MusicSubscriberServer;

import Database.DatabaseServer;
import fontyspublisher.RemotePublisher;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
     * @throws java.net.UnknownHostException
     */
    public static void main(String[] args) throws RemoteException, UnknownHostException
    {
        // Create an instance of RemotePublisher
        RemotePublisher remotePublisher = new RemotePublisher();
        System.setProperty("java.rmi.server.hostname",IpAdresses.getServerIpAdress());
        System.out.println(InetAddress.getLocalHost().getHostAddress());
        // Create registry and register remote publisher
        Registry registry = LocateRegistry.createRegistry(portNumber);
        registry.rebind(bindingName, remotePublisher);

        // Remote publisher registered
        System.out.println("Remote publisher registered.");
        System.out.println("Port number  : " + portNumber);
        System.out.println("Binding name : " + bindingName);
        System.out.println((char)27 + "[32mMain Server running!");
    }
}
