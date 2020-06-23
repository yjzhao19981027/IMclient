package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sample.dao.UserDao;
import sample.dao.UserDaoImpl;
import sample.entity.User;
import sample.util.ImgUtil;
import sample.util.Storage;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CallController implements Initializable {
    @FXML
    public Pane CallPane;
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
