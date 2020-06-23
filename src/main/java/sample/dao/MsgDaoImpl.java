package sample.dao;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import sample.entity.Msg;
import sample.entity.User;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MsgDaoImpl extends DaoImpl {
    @Override
    public List<Msg> getMsg(String userName, String friendName) {
        sqlSession.commit();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("name1", userName);
        param.put("name2", friendName);
        return this.sqlSession.selectList("Mapper.getMsg",param);
    }

    @Override
    public void sendMsg(Msg msg) {
        this.sqlSession.insert("Mapper.sendMsg",msg);
        sqlSession.commit();
    }

    @Override
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
        sqlSession.commit();
    }

    @Override
    public void delChatMsg(String senderName, String receiverName){
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("senderName", senderName);
        param.put("receiverName", receiverName);
        this.sqlSession.delete("Mapper.delChatMsg",param);
        sqlSession.commit();
    }

}
