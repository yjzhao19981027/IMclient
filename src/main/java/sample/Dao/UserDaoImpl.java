package sample.Dao;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import sample.Entity.Msg;
import sample.Entity.User;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDaoImpl implements UserDao{
    public SqlSession sqlSession;


    public UserDaoImpl(){
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

    public User getUserByUserName(String userName){
        sqlSession.commit();
        return this.sqlSession.selectOne("Mapper.getUserByUserName",userName);
    }

    public void registerUser(User user){
        this.sqlSession.insert("Mapper.registerUser",user);
        sqlSession.commit();
    }

    public void changeInfo(User user){
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userName", user.getUserName());
        param.put("password", user.getPassword());
        param.put("sex", user.getSex());
        param.put("motto", user.getMotto());
        param.put("birthday", user.getBirthday());
        param.put("headImg", user.getHeadImg());
        this.sqlSession.update("Mapper.changeInfo",param);
        sqlSession.commit();
    }

    public User getUserByUserName_Password(String userName,String password) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userName", userName);
        param.put("password", password);
        sqlSession.commit();
        return this.sqlSession.selectOne("Mapper.getUserByUserName_Password",param);
    }

    public String getHeadByUserName(String userName){
        sqlSession.commit();
        return this.sqlSession.selectOne("Mapper.getHeadByUserName",userName);
    }

    public List<User> getAllFriends(String userName){
        sqlSession.commit();
        return this.sqlSession.selectList("Mapper.getAllFriends",userName);
    }

    public List<String> getAllChat(String userName) {
        sqlSession.commit();
        return this.sqlSession.selectList("Mapper.getAllChat",userName);
    }

    public List<Msg> getMsg(String userName, String friendName) {
        sqlSession.commit();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("name1", userName);
        param.put("name2", friendName);
        return this.sqlSession.selectList("Mapper.getMsg",param);
    }
    //  发送消息
    public void sendMsg(Msg msg) {
        this.sqlSession.insert("Mapper.sendMsg",msg);
        sqlSession.commit();
    }
    //  获取未阅消息数
    public int getUnreadMsgNum(String senderName, String receiverName) {
        sqlSession.commit();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("name1", senderName);
        param.put("name2", receiverName);
        return this.sqlSession.selectOne("Mapper.getUnreadMsgNum", param);
    }

    public int judgeIsFriend(String userName,String friendName){
        sqlSession.commit();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userName", userName);
        param.put("friendName", friendName);
        return this.sqlSession.selectOne("Mapper.judgeIsFriend",param);
    }

    //  设置消息已阅
    @Override
    public void setMsgIsRead(String senderName, String receiverName) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("name1", senderName);
        param.put("name2", receiverName);
        this.sqlSession.update("Mapper.setMsgIsRead",param);
        sqlSession.commit();
    }

    public void addFriend(String userName,String friendName){
        sqlSession.commit();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userName", userName);
        param.put("friendName", friendName);
        this.sqlSession.insert("Mapper.addFriend",param);
    }

    public void delChatMsg(String senderName, String receiverName){
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("senderName", senderName);
        param.put("receiverName", receiverName);
        this.sqlSession.delete("Mapper.delChatMsg",param);
        sqlSession.commit();
    }

    public void delFriend(String userName, String friendName){
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userName", userName);
        param.put("friendName", friendName);
        this.sqlSession.delete("Mapper.delFriend",param);
        sqlSession.commit();
    }
}
