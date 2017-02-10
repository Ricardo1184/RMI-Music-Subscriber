package musicsubscriber.MainScreen;

import Database.DatabaseController;
import Database.DatabaseMediator;
import FileWatcher.FileWatcher;
import MusicSubscriberServer.Communicator;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ricardo van Dijke
 */
public class FXMLMainScreenController implements Initializable {

    @FXML
    TextField txtMusicFolder;

    @FXML
    Button btnBrowseMusicFolder;

    @FXML
    Button btnConnectToServer;

    @FXML
    Label lblConnected;

    @FXML
    Button btnAddArtist;

    @FXML
    TextField txtArtistName;

    @FXML
    ListView lvArtistNames;

    @FXML
    ListView lvArtistUpdates;

    private Communicator comm;

    private String userName;
    private DatabaseController database;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        try
        {
            comm = new Communicator(this);
        }
        catch (RemoteException ex)
        {
            Logger.getLogger(FXMLMainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public void SetDatabaseController(DatabaseController db)
    {
        database = db;
    }

    public void HandlebtnConnectToServerPressed() throws RemoteException
    {
        if (comm.connectToPublisher())
        {
            lblConnected.setTextFill(Color.GREEN);
            lblConnected.setText("Connected!");
            for (String artist : database.getDatabase().getArtists(""))
            {
                comm.register(artist);
                if (database.getDatabase().getArtistSubscription(userName, artist))
                {
                    comm.subscribe(artist);
                }
            }
        }
        else
        {
            lblConnected.setTextFill(Color.RED);
            lblConnected.setText("Not connected!");
        }
    }

    EventHandler cbChecked = (EventHandler<ActionEvent>) new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event)
        {
            if (event.getSource() instanceof CheckBox)
            {
                CheckBox chk = (CheckBox) event.getSource();
                try
                {
                    if (chk.isSelected())
                    {
                        database.getDatabase().AddArtistSubscription(userName, chk.getText());
                    }
                    else
                    {
                        database.getDatabase().RemoveArtistSubscription(userName, chk.getText());
                    }
                }
                catch (RemoteException ex)
                {
                    Logger.getLogger(FXMLMainScreenController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    };

    public void HandletxtArtistsChanged() throws RemoteException
    {
        lvArtistNames.getItems().clear();
        for (String artistName : database.getDatabase().getArtists(txtArtistName.getText()))
        {
            Pane pane = new Pane();
            pane.setPrefSize(150, 20);
            CheckBox checkBox = new CheckBox();
            checkBox.setText(artistName);
            checkBox.setSelected(database.getDatabase().getArtistSubscription(userName, artistName));
            checkBox.setOnAction(cbChecked);
            pane.getChildren().add(checkBox);
            lvArtistNames.getItems().add(pane);
        }
    }

    public void HandlebtnBrowseFolderPressed(ActionEvent e)
    {
        try
        {
            DirectoryChooser dc = new DirectoryChooser();
            File selectedDirectory = dc.showDialog(new Stage());

            Thread watcherThread = null;
            watcherThread = new Thread(new FileWatcher(selectedDirectory.toPath(), this));
            watcherThread.start();
            txtMusicFolder.setText(selectedDirectory.toPath().toString());
            for (File dir : selectedDirectory.listFiles())
            {
                if (dir.isDirectory())
                {
                    if (database.getDatabase().AddArtist(dir.getName()))
                    {
                        database.getDatabase().AddArtistSubscription(userName,dir.getName());
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText(dir.getName() + " was added to the database");
                        HandletxtArtistsChanged();
                        alert.showAndWait();
                    }
                }
            }
        }
        catch (IOException ex)
        {
            Logger.getLogger(FXMLMainScreenController.class.getName()).log(Level.SEVERE, null, ex);
            Alert alertBox = new Alert(Alert.AlertType.ERROR);
            alertBox.setContentText("The specified Path does not exist");
            alertBox.showAndWait();
        }
    }

    public void addArtistUpdate(String artist, String type)
    {
        lvArtistUpdates.getItems().add(artist + " was " + type);
    }

    public FXMLMainScreenController()
    {
    }

    public void PushUpdate(WatchEvent<Path> event)
    {

        Platform.runLater(new Runnable() {
            @Override
            public void run()
            {
                String eventType = "";
                WatchEvent.Kind type = event.kind();
                if (type == StandardWatchEventKinds.ENTRY_CREATE)
                {
                    eventType = "created";
                }
                else if (type == StandardWatchEventKinds.ENTRY_DELETE)
                {
                    eventType = "deleted";
                }
                else if (type == StandardWatchEventKinds.ENTRY_MODIFY)
                {
                    eventType = "modified";
                }
                lvArtistUpdates.getItems().add(event.context().getFileName() + " was " + eventType);
                broadcastArtistChanged(event.context().getFileName().toString(), eventType);
            }

        });
    }

    private void broadcastArtistChanged(String property, String type)
    {
        comm.broadcast(property, type);
    }

}
