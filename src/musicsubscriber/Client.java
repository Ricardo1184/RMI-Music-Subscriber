package musicsubscriber;

import musicsubscriber.Login.FXMLLoginController;
import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Pajama Sammy
 */
public class Client extends Application {
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        
        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("Login/FXMLLogin.fxml"));
        Pane myPane = (Pane) myLoader.load();
        FXMLLoginController controller = (FXMLLoginController) myLoader.getController();
        controller.setPrevStage(primaryStage);

        Scene myScene = new Scene(myPane);
        primaryStage.setScene(myScene);
        primaryStage.initStyle(StageStyle.UTILITY);
     
        primaryStage.setTitle("Music Subscriber");
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }  
}
