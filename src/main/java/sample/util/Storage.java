package sample.util;

import io.netty.channel.Channel;
import sample.controller.*;
import sample.entity.User;


public class Storage {
    public static User user;
    public static String callFriend;
    public static LoginController loginController;
    public static ChatController chatController;
    public static CallController callController;
    public static String INET_HOST = "127.0.0.1";
    public static int INET_PORT = 7397;
    public static Channel channel;
    public static boolean onlineFlag = false;
    public static double xOffset = 550;
    public static double yOffset = 550;
}
