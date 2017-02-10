package MusicSubscriberServer;

import fontyspublisher.IRemotePropertyListener;
import fontyspublisher.IRemotePublisherForDomain;
import fontyspublisher.IRemotePublisherForListener;
import java.beans.PropertyChangeEvent;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import musicsubscriber.MainScreen.FXMLMainScreenController;

/**
 * Communicator for white board. Establishes connection with Remote Publisher.
 *
 * @author Nico Kuijpers
 */
public class Communicator
        extends UnicastRemoteObject
        implements IRemotePropertyListener
{

    // Reference to whiteboard
    private final FXMLMainScreenController controller;

    // Remote publisher
    private IRemotePublisherForDomain publisherForDomain;
    private IRemotePublisherForListener publisherForListener;
    private static int portNumber = 1099;
    private static String bindingName = "publisher";
    private boolean connected = false;

    // Thread pool
    private final int nrThreads = 10;
    private ExecutorService threadPool = null;

    /**
     * Constructor.
     *
     * @param controller
     * @throws java.rmi.RemoteException
     */
    public Communicator(FXMLMainScreenController controller) throws RemoteException
    {
        this.controller = controller;
        threadPool = Executors.newFixedThreadPool(nrThreads);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) throws RemoteException
    {
        String artist = evt.getPropertyName();
        String type = (String) evt.getNewValue();
        controller.addArtistUpdate(artist, type);
    }

    /**
     * Establish connection with remote publisher.
     */
    public boolean connectToPublisher()
    {
        try
        {
            Registry registry = LocateRegistry.getRegistry(IpAdresses.getServerIpAdress(), portNumber);
            publisherForDomain = (IRemotePublisherForDomain) registry.lookup(bindingName);
            publisherForListener = (IRemotePublisherForListener) registry.lookup(bindingName);
            connected = true;

            System.out.println("Connection with remote publisher established");
            return connected;
        } catch (RemoteException | NotBoundException ex)
        {
            connected = false;
            System.err.println("Cannot establish connection to remote publisher");
            System.err.println("Run Server to start remote publisher");
            return connected;
        }
    }

    /**
     * Register property at remote publisher.
     *
     * @param property property to be registered
     */
    public void register(String property)
    {
        if (connected)
        {
            try
            {
                // Nothing changes at remote publisher in case property was already registered
                publisherForDomain.registerProperty(property);
            } catch (RemoteException ex)
            {
                Logger.getLogger(Communicator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Subscribe to property.
     *
     * @param property property to subscribe to
     */
    public void subscribe(String property)
    {
        if (connected)
        {
            final IRemotePropertyListener listener = this;
            //threadPool.execute(new Runnable()
            //{
            //    @Override
            //    public void run()
            //    {
                    try
                    {
                        publisherForListener.subscribeRemoteListener(listener, property);
                    } catch (RemoteException ex)
                    {
                        Logger.getLogger(Communicator.class.getName()).log(Level.SEVERE, null, ex);
                    }
              //  }

           // });
        }
    }

    /**
     * Unsubscribe to property.
     *
     * @param property property to unsubscribe to
     */
    public void unsubscribe(String property)
    {
        if (connected)
        {
            final IRemotePropertyListener listener = this;
            threadPool.execute(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        publisherForListener.unsubscribeRemoteListener(listener, property);
                    } catch (RemoteException ex)
                    {
                        Logger.getLogger(Communicator.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            });
        }
    }

    /**
     * Broadcast draw event.
     *
     * @param property color of draw event
     * @param album
     */
    public void broadcast(String property, String type)
    {
        if (connected)
        {
            threadPool.execute(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        publisherForDomain.inform(property, null, type);
                        System.out.println("broadcast " + property + " " + type);
                    } catch (RemoteException ex)
                    {
                        Logger.getLogger(Communicator.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            });
        }
    }

    /**
     * Stop communicator.
     */
    public void stop()
    {
        if (connected)
        {
            try
            {
                publisherForListener.unsubscribeRemoteListener(this, null);
            } catch (RemoteException ex)
            {
                Logger.getLogger(Communicator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try
        {
            UnicastRemoteObject.unexportObject(this, true);
        } catch (NoSuchObjectException ex)
        {
            Logger.getLogger(Communicator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
