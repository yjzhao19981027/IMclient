package sample.Util;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import sample.Controller.*;
import sample.Entity.User;
import sample.Socket.NettyClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Storage {
    public static User user;
    public static LoginController loginController;
    public static ChatController chatController;
    public static String inetHost = "127.0.0.1";
    public static int inetPort = 7397;
    public static Channel channel;
    public static boolean onlineFlag = false;
}
