package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sample.dao.Dao;
import sample.dao.UserDaoImpl;
import sample.entity.User;
import sample.util.DateUtil;
import sample.util.ImgUtil;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {
    @FXML
    private Pane registerPane;
    @FXML
    private TextField userName;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField comfirmPassword;
    @FXML
    private RadioButton maleButton;
    @FXML
    private RadioButton femaleButton;
    @FXML
    private DatePicker birthday;
    @FXML
    private TextArea motto;

    private Dao usrDao;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        usrDao = new UserDaoImpl();
    }

    //  最小化按钮
    public void minAction(ActionEvent actionEvent){
        Stage stage = (Stage) registerPane.getScene().getWindow();
        stage.setIconified(true);
    }

    public void closeAction(ActionEvent actionEvent){
        Stage stage = (Stage) registerPane.getScene().getWindow();
        stage.close();
    }

    public void maleButtonAction(ActionEvent actionEvent) {
        maleButton.setSelected(true);
        femaleButton.setSelected(false);
    }

    public void femaleButtonAction(ActionEvent actionEvent) {
        maleButton.setSelected(false);
        femaleButton.setSelected(true);
    }

    public void register_buttonAction(ActionEvent actionEvent) throws ParseException, URISyntaxException {
        String userName = this.userName.getText();
        String password = this.password.getText();
        String comfirmPassword = this.comfirmPassword.getText();
        String sex = maleButton.isSelected() ? "male" : "female";
        Date birthday = this.birthday.getValue() != null ? DateUtil.localDate2Date(this.birthday.getValue()) : null;
        String motto = this.motto.getText();
        //  信息不完整
        if (userName.equals("") || password.equals("") || comfirmPassword.equals("")
        || sex.equals("") || birthday == null || motto.equals("")){
            Stage stage = new Stage();
            Label l = new Label("信息不能为空!");
            Scene s = new Scene(l,200,124);
            stage.setScene(s);
            stage.show();
            return;
        }
        //  用户名已存在
        if (usrDao.getUserByUserName(userName) != null){
            Stage stage = new Stage();
            Label l = new Label("用户名已存在!");
            Scene s = new Scene(l,200,124);
            stage.setScene(s);
            stage.show();
            return;
        }
        //  密码和确认密码不同
        if (!password.equals(comfirmPassword)){
            Stage stage = new Stage();
            Label l = new Label("密码两次输入不同!");
            Scene s = new Scene(l,200,124);
            stage.setScene(s);
            stage.show();
            return;
        }
        String url = "fxml/img/gui/head_default.png";
        File file = new File(this.getClass().getResource("/").toURI().getPath() + url);
        User user = new User("1",userName,password,sex,motto,birthday,false, ImgUtil.imageToBase64(file));
        usrDao.registerUser(user);
        Stage stage = (Stage) registerPane.getScene().getWindow();
        stage.close();
    }
}
