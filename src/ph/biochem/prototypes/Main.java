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
        Parent root = FXMLLoader.load(getClass().getResource("../resources/Login.fxml"));
        //primaryStage.initStyle(StageStyle.UNIFIED);
        primaryStage.setTitle("BioChem Healthcare Services");
        primaryStage.setScene(new Scene(root, 329, 291));
        primaryStage.show();
    }
}
