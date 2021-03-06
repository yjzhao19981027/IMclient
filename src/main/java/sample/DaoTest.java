package sample;


import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import sample.dao.Dao;
import sample.dao.UserDaoImpl;
import sample.entity.Msg;
import sample.entity.User;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class DaoTest {
    public static void main(String[] args) throws IOException {
        String resources="config/mybatis-config.xml";
        InputStream inputStream= Resources.getResourceAsStream(resources);
        SqlSessionFactory sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession=sqlSessionFactory.openSession(true);

        Dao tool = new UserDaoImpl();

        List<Msg> list = tool.getMsg("Prodigal_son","Elio");
       // tool.setMsgIsRead("Prodigal_son","Peter");
        User user = tool.getUserByUserName("Prodigal_son");
//        List<User> list = tool.getAllFriends("Prodigal_son");
//        if (list.get(0).getImg() == null)
//            System.out.println(list.get(0).isRead());
//        System.out.println(list.get(1).getMsg());
        System.out.println(user.getUserName());
        System.out.println(user.getPassword());
        System.out.println(user.getSex());
        System.out.println(user.getMotto());
        System.out.println(user.getBirthday());
        System.out.println(user.getUserId());
        System.out.println(user.getOnline());
//        System.out.println(user.getHeadImg());
    }
}



