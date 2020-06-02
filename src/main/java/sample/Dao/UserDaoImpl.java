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

    public User getUserByUserName_Password(String userName,String password) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userName", userName);
        param.put("password", password);
        sqlSession.commit();
        return this.sqlSession.selectOne("Mapper.getUserByUserName_Password",param);
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

    public void sendMsg(Msg msg) {
        this.sqlSession.insert("Mapper.sendMsg",msg);
    }

    public int getUnreadMsgNum(String senderName, String receiverName) {
        sqlSession.commit();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("name1", senderName);
        param.put("name2", receiverName);
        return this.sqlSession.selectOne("Mapper.getUnreadMsgNum", param);
    }

    @Override
    public void setMsgIsRead(String senderName, String receiverName) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("name1", senderName);
        param.put("name2", receiverName);
        this.sqlSession.update("Mapper.setMsgIsRead",param);
    }
}
