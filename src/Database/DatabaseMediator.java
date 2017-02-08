package Database;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pajama Sammy
 */
public class DatabaseMediator extends UnicastRemoteObject implements IPersistencyMediator
{

    private static Connection con;
    //private static Statement statement;
    private static ResultSet myRs;

    private static final String connectionstring = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String user = "system";
    private static final String pass = "admin";

    public DatabaseMediator() throws RemoteException
    {
        try
        {
            con = DriverManager.getConnection(connectionstring, user, pass);
            System.out.println(con.toString());
        } catch (SQLException ex)
        {
            Logger.getLogger(DatabaseMediator.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     *
     * @param userName
     */
    @Override
    public void AddUser(String userName) throws RemoteException
    {
        try
        {
            Statement statement = con.createStatement();
            String query = "INSERT INTO ACCOUNT(Name) VALUES('" + userName + "')";
            statement.executeQuery(query);
        } catch (Exception ex)
        {
            Logger.getLogger(DatabaseMediator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean LogIn(String userName) throws RemoteException
    {
        try
        {
            Statement statement = con.createStatement();
            String query = "SELECT Name from ACCOUNT WHERE Name = '" + userName + "'";
            statement.executeQuery(query);
            myRs = statement.getResultSet();

            if (myRs.next())
            {
                return true;
            }

        } catch (Exception ex)
        {
            Logger.getLogger(DatabaseMediator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;

    }

    /**
     *
     * @param artist
     * @return
     */
    @Override
    public boolean AddArtist(String artist) throws RemoteException
    {
        try
        {
            Statement statement = con.createStatement();
            String query = "INSERT INTO ARTIST(Name) VALUES('" + artist + "')";
            statement.executeQuery(query);
        } catch (Exception ex)
        {
            Logger.getLogger(DatabaseMediator.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    @Override
    public synchronized ArrayList<String> getArtists(String artistName) throws RemoteException
    {
        ArrayList<String> artists = new ArrayList<>();
        try
        {
            Statement statement = con.createStatement();
            String query = "SELECT Name from Artist WHERE Name LIKE '%" + artistName + "%'";
            statement.executeQuery(query);
            myRs = statement.getResultSet();

            while (myRs.next())
            {
                artists.add(myRs.getString("Name"));
            }

        } catch (Exception ex)
        {
            Logger.getLogger(DatabaseMediator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return artists;
    }

    @Override
    public boolean AddArtistSubscription(String userName, String artist) throws RemoteException
    {
        try
        {
            Statement statement = con.createStatement();
            String query = "INSERT INTO SUBSCRIPTION(Account_Name, Artist_Name) VALUES('" + userName + "','" + artist + "')";
            statement.executeQuery(query);
        } catch (Exception ex)
        {
            Logger.getLogger(DatabaseMediator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    @Override
    public boolean RemoveArtistSubscription(String userName, String artist) throws RemoteException
    {
        try
        {
            Statement statement = con.createStatement();
            String query = "DELETE FROM SUBSCRIPTION WHERE Account_Name  = '" + userName + "' AND Artist_Name ='" + artist + "'";
            statement.executeQuery(query);
        } catch (Exception ex)
        {
            Logger.getLogger(DatabaseMediator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    @Override
    public boolean getArtistSubscription(String userName, String artist) throws RemoteException
    {
        try
        {
            Statement statement = con.createStatement();
            String query = "SELECT * from SUBSCRIPTION WHERE Account_Name = '" + userName + "' AND Artist_Name = '" + artist + "'";
            statement.executeQuery(query);
            myRs = statement.getResultSet();

            if (myRs.next())
            {
                return true;
            }

        } catch (Exception ex)
        {
            Logger.getLogger(DatabaseMediator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
