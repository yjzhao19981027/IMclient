package sample.Entity;

public class User {
    private String UserId;
    private String UserName;
    private String Sex;
    private String Password;
    private String motto;
    private int age;
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

    public int getAge() {
        return age;
    }

    public boolean getOnline() {
        return online;
    }

    public String getHeadImg() {
        return headImg;
    }

    public User(String userId, String userName, String password, String sex, String motto, int age, boolean online, String headImg) {
        UserId = userId;
        UserName = userName;
        Password = password;
        this.Sex = sex;
        this.motto = motto;
        this.age = age;
        this.online = online;
        this.headImg = headImg;
    }
}
