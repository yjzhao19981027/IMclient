package sample.dao;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import sample.entity.Msg;
import sample.entity.User;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface Dao {
    public User getUserByUserName(String userName);
    public void registerUser(User user);

    public void changeInfo(User user);
    //  通过用户名密码获取用户 返回值：User
    public User getUserByUserName_Password(String userName,String password);
    public String getHeadByUserName(String userName);
    //  通过用户名返回好友    返回值：List<User>
    public List<User> getAllFriends(String userName);
    public List<String> getAllChat(String userName);
    public List<Msg> getMsg(String userName,String friendName);
    public void sendMsg(Msg msg);
    public int getUnreadMsgNum(String senderName,String receiverName);
    public int judgeIsFriend(String userName,String friendName);
    public void setMsgIsRead(String senderName, String receiverName);
    public void addFriend(String userName,String friendName);
    public void delFriend(String userName, String friendName);
    public void delChatMsg(String senderName, String receiverName);
}
