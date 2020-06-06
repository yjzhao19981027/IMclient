package sample.Entity;

import java.util.Date;

public class User {
    private String UserId;
    private String UserName;
    private String Sex;
    private String Password;
    private String motto;
    private Date birthday;
    private boolean online;
    private String headImg;

    public String getUserId() {
        return UserId;
    }

    public String getUserName() {
        return UserName;
    }

    public String getPassword() {
        return Password;
    }

    public String getSex() {
        return Sex;
    }

    public String getMotto() {
        return motto;
    }

    public Date getBirthday() {
        return birthday;
    }

    public boolean getOnline() {
        return online;
    }

    public String getHeadImg() {
        return headImg;
    }

    public User(String userId, String userName, String password, String sex, String motto, Date birthday, boolean online, String headImg) {
        UserId = userId;
        UserName = userName;
        Password = password;
        this.Sex = sex;
        this.motto = motto;
        this.birthday = birthday;
        this.online = online;
        this.headImg = headImg;
    }
}
