package musicsubscriber.Login;

import Database.DatabaseController;
import Database.DatabaseMediator;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import musicsubscriber.MainScreen.FXMLMainScreenController;

/**
 * FXML Controller class
 *
 * @author Pajama Sammy
 */
public class FXMLLoginController implements Initializable
{

    Stage previousStage;

    @FXML
    Button btnLogin;
    @FXML
    Button btnNewUser;
    @FXML
    TextField txtUserName;
    
    DatabaseController database = new DatabaseController();
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        database.connectToRMIDatabaseServer();
    }

    /**
     *
     * @param primaryStage
     */
    public void setPrevStage(Stage primaryStage)
    {
        previousStage = primaryStage;
    }

    public void handleBtnLoginClicked() throws IOException
    {
        if (database.getDatabase().LogIn(txtUserName.getText()))// || true)
        {
            Stage stage = new Stage();
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/musicsubscriber/MainScreen/FXMLMainScreen.fxml"));

            Pane myPane = (Pane) myLoader.load();
            FXMLMainScreenController controller = (FXMLMainScreenController) myLoader.getController();
            controller.setUserName(txtUserName.getText());
            controller.SetDatabaseController(database);
            controller.HandletxtArtistsChanged();
            Scene myScene = new Scene(myPane);
            stage.setScene(myScene);
            stage.setTitle("Music Subscriber");

            stage.show();
            previousStage.close();
        }
    }

    public void handleBtnNewUserClicked()
    {

    }
}
