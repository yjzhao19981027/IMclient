package sample.Util;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import sample.Controller.*;
import sample.Entity.User;
import sample.Socket.NettyClient;


public class Storage {
    public static User user;
    public static String callFriend;
    public static LoginController loginController;
    public static ChatController chatController;
    public static CallController callController;
    public static String inetHost = "127.0.0.1";
    public static int inetPort = 7397;
    public static Channel channel;
    public static boolean onlineFlag = false;
}
