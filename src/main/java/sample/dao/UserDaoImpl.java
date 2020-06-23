package sample.dao;

import sample.entity.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDaoImpl extends DaoImpl{

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

    public int judgeIsFriend(String userName,String friendName){
        sqlSession.commit();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userName", userName);
        param.put("friendName", friendName);
        return this.sqlSession.selectOne("Mapper.judgeIsFriend",param);
    }

    public void addFriend(String userName,String friendName){
        sqlSession.commit();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userName", userName);
        param.put("friendName", friendName);
        this.sqlSession.insert("Mapper.addFriend",param);
    }

    public void delFriend(String userName, String friendName){
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userName", userName);
        param.put("friendName", friendName);
        this.sqlSession.delete("Mapper.delFriend",param);
        sqlSession.commit();
    }
}
