package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        URL path = getClass().getResource("/FXML/Login/login.fxml");
        Parent root = FXMLLoader.load(path);
        primaryStage.setTitle("登录");
        primaryStage.setScene(new Scene(root, 540, 415));
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
