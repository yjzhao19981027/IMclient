package sample.Dao;

import sample.Entity.Msg;
import sample.Entity.User;

import java.util.List;

public interface UserDao {
    public User getUserByUserName_Password(String userName,String password);
    public List<User> getAllFriends(String userName);
    public List<String> getAllChat(String userName);
    public List<Msg> getMsg(String userName,String friendName);
    public void sendMsg(Msg msg);
    public int getUnreadMsgNum(String senderName,String receiverName);
    public void setMsgIsRead(String senderName, String receiverName);
}
