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

public class DaoImpl implements Dao {
    public SqlSession sqlSession;

    public DaoImpl(){
        String resource = "config/mybatis-config.xml";
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession=sqlSessionFactory.openSession(true);
        this.sqlSession = sqlSession;
    }

    @Override
    public User getUserByUserName(String userName) {
        return null;
    }

    @Override
    public void registerUser(User user) {

    }

    @Override
    public void changeInfo(User user) {

    }

    @Override
    public User getUserByUserName_Password(String userName, String password) {
        return null;
    }

    @Override
    public String getHeadByUserName(String userName) {
        return null;
    }

    @Override
    public List<User> getAllFriends(String userName) {
        return null;
    }

    @Override
    public List<String> getAllChat(String userName) {
        return null;
    }

    @Override
    public List<Msg> getMsg(String userName, String friendName) {
        return null;
    }

    @Override
    public void sendMsg(Msg msg) {

    }

    @Override
    public int getUnreadMsgNum(String senderName, String receiverName) {
        return 0;
    }

    @Override
    public int judgeIsFriend(String userName, String friendName) {
        return 0;
    }

    @Override
    public void setMsgIsRead(String senderName, String receiverName) {

    }

    @Override
    public void addFriend(String userName, String friendName) {

    }

    @Override
    public void delFriend(String userName, String friendName) {

    }

    @Override
    public void delChatMsg(String senderName, String receiverName) {

    }
}
