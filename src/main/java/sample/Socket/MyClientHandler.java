package sample.Socket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.Util.ChatWindowUtil;
import sample.Util.CmdUtil;
import sample.Util.Storage;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MyClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        SocketChannel channel = (SocketChannel) ctx.channel();
        System.out.println("链接报告开始");
        System.out.println("链接报告信息：本客户端链接到服务端。channelId：" + channel.id());
        System.out.println("链接报告 IP:" + channel.localAddress().getHostString());
        System.out.println("链接报告 Port:" + channel.localAddress().getPort());
        System.out.println("链接报告完毕");
//         通知客户端链接建立成功
//        String str = "通知服务端链接建立成功" + "" + new Date()+" "+ channel.localAddress().getHostString() +"\r\n";
//        ctx.writeAndFlush(str);
    }
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("断开链接" + ctx.channel().localAddress().toString());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String head = CmdUtil.getCmdHead(msg);
        String content = CmdUtil.getCmdContent(msg);
        // 接收 msg 消息
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "接收到消息：" + msg);
        if (msg.equals("refreshen")){
            Platform.runLater(new Runnable() {
                @Override
                public void run(){
                    try {
                        Storage.chatController.refreshen();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            });
        }
        else if (msg.equals("online")){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Stage primaryStage = new Stage();
                    URL path = getClass().getResource("/FXML/Call/call.fxml");
                    Parent root = null;
                    try {
                        root = FXMLLoader.load(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    primaryStage.setTitle("语音通话");
                    primaryStage.setScene(new Scene(root, 300, 480));
                    try {
                        Storage.callController.initImg();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    primaryStage.initStyle(StageStyle.UNDECORATED);
                    primaryStage.show();
                    ChatWindowUtil.setDragged(root, primaryStage);
                }
            });
        }
        else if (msg.equals("offline")){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Stage stage = new Stage();
                    Label l = new Label("用户不在线!");
                    Scene s = new Scene(l,200,124);
                    stage.setScene(s);
                    stage.show();
                }
            });
        }
        else if (head.equals("call")){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Storage.callFriend = content;
                    Stage primaryStage = new Stage();
                    URL path = getClass().getResource("/FXML/Call/call.fxml");
                    Parent root = null;
                    try {
                        root = FXMLLoader.load(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    primaryStage.setTitle("语音通话");
                    primaryStage.setScene(new Scene(root, 300, 480));
                    try {
                        Storage.callController.initImg();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    primaryStage.initStyle(StageStyle.UNDECORATED);
                    primaryStage.show();
                    ChatWindowUtil.setDragged(root, primaryStage);
                }
            });
        }
        else if (head.equals("cancel")  && content.equals(Storage.callFriend)){
            System.out.println("cancel");
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Stage stage = (Stage) Storage.callController.CallPane.getScene().getWindow();
                    stage.close();
                }
            });
        }
    }

    /**
     * 抓住异常，当发生异常的时候，可以做一些相应的处理，比如打印日志、关闭链接
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {ctx.close();
        System.out.println("异常信息：\r\n" + cause.getMessage());
    }
}
