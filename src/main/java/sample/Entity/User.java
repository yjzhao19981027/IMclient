package sample.Entity;

public class User {
    private String UserId;
    private String UserName;
    private String Sex;
    private String Password;
    private String motto;
    private int age;
    private boolean Online;

    public boolean getOnline() {
        return Online;
    }

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

    public int getAge() {
        return age;
    }

    public boolean isOnline() {
        return online;
    }

    private boolean online;

    public User(String userId, String userName, String password, String sex, String motto, int age, boolean online) {
        UserId = userId;
        UserName = userName;
        Password = password;
        this.Sex = sex;
        this.motto = motto;
        this.age = age;
        this.online = online;
    }
}
