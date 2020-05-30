package sample;


import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import sample.Dao.UserDao;
import sample.Dao.UserDaoImpl;
import sample.Entity.Msg;
import sample.Entity.User;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

public class DaoTest {
    public static void main(String[] args) throws IOException {
        String resources="config/mybatis-config.xml";
        InputStream inputStream= Resources.getResourceAsStream(resources);
        SqlSessionFactory sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession=sqlSessionFactory.openSession(true);

        UserDao tool = new UserDaoImpl();

//        List<Msg> list = tool.getMsg("Prodigal_son","Elio");
        Msg msg = new Msg("Prodigal_son","Peter","dog",new Date(),"person");
        tool.sendMsg(msg);
//        User user = tool.getUserByUserName_Password("Prodigal_son","zyj19981027");
//        List<User> list = tool.getAllFriends("Prodigal_son");
//        System.out.println(list.get(0).getMsg());
//        System.out.println(list.get(1).getMsg());
//        System.out.println(user.getUserName());
//        System.out.println(user.getPassword());
//        System.out.println(user.getSex());
//        System.out.println(user.getMotto());
//        System.out.println(user.getAge());
//        System.out.println(user.getUserId());
//        System.out.println(user.getOnline());
    }
}


