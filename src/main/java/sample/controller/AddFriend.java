package sample.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sample.dao.Dao;
import sample.dao.UserDaoImpl;
import sample.entity.User;
import sample.util.ImgUtil;
import sample.util.Storage;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class AddFriend implements Initializable {
    @FXML
    private Pane addFriendPane;
    @FXML
    private Label user_searchAdd;
    @FXML
    private TextField user_search;
    @FXML
    private Label userName;
    @FXML
    private Label age;
    @FXML
    private Label sex;
    @FXML
    private Label motto;
    @FXML
    private Button add_friend;
    @FXML
    private Label isFriend;
    @FXML
    private ImageView headImg;
    @FXML
    private Pane content;

    private Dao usrDao;

    private User user;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        usrDao = new UserDaoImpl();
        user_searchAdd.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    content.setVisible(true);
                    String content = user_search.getText();
                    user = usrDao.getUserByUserName(content);
                    if (user == null || user.getUserName().equals(Storage.user.getUserName()))
                        return;
                    try {
                        headImg.setImage(ImgUtil.base64toImage(user.getHeadImg()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    userName.setText("用户名：" + user.getUserName());
                    age.setText("年龄：" + String.valueOf(new Date().getYear() - user.getBirthday().getYear()));
                    String s = user.getSex().equals("male") ? "男" : "女";
                    sex.setText("性别：" + s);
                    motto.setText("个性签名：" + user.getMotto());
                    boolean judge = usrDao.judgeIsFriend(Storage.user.getUserName(), user.getUserName()) != 0;
                    if (judge){
                        isFriend.setVisible(true);
                        add_friend.setVisible(false);
                    }
                    else{
                        isFriend.setVisible(false);
                        add_friend.setVisible(true);
                    }
                }
            }
        });
    }

    //  最小化按钮
    public void minAction(ActionEvent actionEvent){
        Stage stage = (Stage) addFriendPane.getScene().getWindow();
        stage.setIconified(true);
    }

    public void closeAction(ActionEvent actionEvent){
        Stage stage = (Stage) addFriendPane.getScene().getWindow();
        stage.close();
    }

    public void addFriendAction(ActionEvent actionEvent) throws IOException{
        usrDao.addFriend(user.getUserName(),Storage.user.getUserName());
        usrDao.addFriend(Storage.user.getUserName(),user.getUserName());
        isFriend.setVisible(true);
        add_friend.setVisible(false);
        Storage.chatController.refreshen();
        Storage.channel.writeAndFlush("addFriend " + user.getUserName() + "\r\n");
    }
}
