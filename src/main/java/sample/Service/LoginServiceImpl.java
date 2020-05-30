package sample.Service;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import sample.Dao.UserDao;
import sample.Dao.UserDaoImpl;
import sample.Entity.User;

import java.io.IOException;
import java.io.InputStream;

public class LoginServiceImpl implements LoginService {
    UserDao tool;

    public LoginServiceImpl() throws IOException {
        tool = new UserDaoImpl();
    }
    public boolean login(String userName, String password) {
        User user = tool.getUserByUserName_Password(userName,password);
        if (user == null)
            return false;
        return true;
    }
}
