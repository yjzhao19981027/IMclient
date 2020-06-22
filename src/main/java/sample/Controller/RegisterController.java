package sample.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sample.Dao.UserDao;
import sample.Dao.UserDaoImpl;
import sample.Entity.User;
import sample.Util.DateUtil;
import sample.Util.ImgUtil;

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

    private UserDao dao;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dao = new UserDaoImpl();
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
        if (dao.getUserByUserName(userName) != null){
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
        String url = "./FXML/Img/gui/head_default.png";
        File file = new File(this.getClass().getResource("/").toURI().getPath() + url);
        User user = new User("1",userName,password,sex,motto,birthday,false, ImgUtil.imageToBase64(file));
        dao.registerUser(user);
        Stage stage = (Stage) registerPane.getScene().getWindow();
        stage.close();
    }
}
