package ph.biochem.prototypes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../resources/MainMenu.fxml"));
        //primaryStage.initStyle(StageStyle.UNIFIED);
        primaryStage.setTitle("BioChem Angeles");
        primaryStage.setScene(new Scene(root, 1209, 644));
        primaryStage.show();
    }
}
