package sample;

import io.netty.channel.Channel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.Socket.NettyClient;

import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        URL path = getClass().getResource("/FXML/Login/login.fxml");
        Parent root = FXMLLoader.load(path);
        primaryStage.setTitle("登录");
        primaryStage.setScene(new Scene(root, 540, 415));
        primaryStage.show();

        NettyClient nettyClient = new NettyClient();
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<Channel> future = executorService.submit(nettyClient);
        Channel channel = future.get();
        if (null != channel){
            System.out.println("start!");
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
