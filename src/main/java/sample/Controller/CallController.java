package sample.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sample.Dao.UserDao;
import sample.Dao.UserDaoImpl;
import sample.Entity.User;
import sample.Util.ImgUtil;
import sample.Util.Storage;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CallController implements Initializable {
    @FXML
    public Pane CallPane;
    @FXML
    private Label connecting;
    @FXML
    private ImageView headImg;

    private UserDao dao;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Storage.callController = CallController.this;
        dao = new UserDaoImpl();
    }

    public void initImg() throws IOException {
        User friend = dao.getUserByUserName(Storage.callFriend);
        Image image = ImgUtil.base64toImage(friend.getHeadImg());
        headImg.setImage(image);
    }

    public void disconnectAction(ActionEvent actionEvent) {
        Storage.channel.writeAndFlush("cancel " + Storage.callFriend + "\r\n");
        Stage stage = (Stage) CallPane.getScene().getWindow();
        stage.close();
    }
}
