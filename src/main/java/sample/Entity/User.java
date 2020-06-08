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

    public void setUserId(String userId) {
        UserId = userId;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
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
