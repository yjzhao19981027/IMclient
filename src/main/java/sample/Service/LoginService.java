package sample.Service;

import sample.Dao.UserDao;

public interface LoginService {
    public boolean login(String userName,String password);
}
