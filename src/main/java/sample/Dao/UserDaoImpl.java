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
        return this.sqlSession.selectOne("Mapper.getUserByUserName_Password",param);
    }

    public List<User> getAllFriends(String userName){
        return this.sqlSession.selectList("Mapper.getAllFriends",userName);
    }

    public List<String> getAllChat(String userName) {
        return this.sqlSession.selectList("Mapper.getAllChat",userName);
    }

    public List<Msg> getMsg(String userName, String friendName) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("name1", userName);
        param.put("name2", friendName);
        return this.sqlSession.selectList("Mapper.getMsg",param);
    }

    public void sendMsg(Msg msg) {
        this.sqlSession.insert("Mapper.sendMsg",msg);
    }


}
