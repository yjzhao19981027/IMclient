package sample.Controller;

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
import sample.Dao.UserDao;
import sample.Dao.UserDaoImpl;
import sample.Entity.User;
import sample.Util.ImgUtil;
import sample.Util.Storage;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class AddFriend implements Initializable {
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

    private UserDao dao;

    private User user;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dao = new UserDaoImpl();
        user_searchAdd.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    String content = user_search.getText();
                    user = dao.getUserByUserName(content);
                    if (user == null || user.getUserName().equals(Storage.user.getUserName()))
                        return;
                    try {
                        headImg.setImage(ImgUtil.base64toImage(user.getHeadImg()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    userName.setText("用户名：" + user.getUserName());
                    age.setText("年龄：" + String.valueOf(new Date().getYear() - user.getBirthday().getYear()));
                    sex.setText("性别：" + user.getSex());
                    motto.setText("个性签名：" + user.getMotto());
                    boolean judge = dao.judgeIsFriend(Storage.user.getUserName(), user.getUserName()) != 0;
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

    public void addFriendAction(ActionEvent actionEvent) throws IOException{
        dao.addFriend(user.getUserName(),Storage.user.getUserName());
        dao.addFriend(Storage.user.getUserName(),user.getUserName());
        isFriend.setVisible(true);
        add_friend.setVisible(false);
        Storage.chatController.refreshen();
        Storage.channel.writeAndFlush("addFriend " + user.getUserName() + "\r\n");
    }
}
