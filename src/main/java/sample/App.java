package sample;

import io.netty.channel.Channel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.socket.NettyClient;
import sample.util.ChatWindowUtil;

import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        URL path = getClass().getResource("/fxml/login/login.fxml");
        Parent root = FXMLLoader.load(path);
        primaryStage.setTitle("登录");
        primaryStage.setScene(new Scene(root, 540, 479));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
        ChatWindowUtil.setDragged(root, primaryStage);

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
