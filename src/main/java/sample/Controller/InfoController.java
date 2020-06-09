package sample.Controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.Dao.UserDao;
import sample.Dao.UserDaoImpl;
import sample.Entity.User;
import sample.Util.DateUtil;
import sample.Util.ImgUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

public class InfoController implements Initializable {
    @FXML
    private ImageView headImg;
    @FXML
    private Label changeImg;
    @FXML
    private Label userName;
    @FXML
    private TextField pwd;
    @FXML
    private RadioButton maleButton;
    @FXML
    private RadioButton femaleButton;
    @FXML
    private DatePicker birthday;
    @FXML
    private TextField motto;

    private ImgUtil imgUtil;

    private DateUtil dateUtil;

    private UserDao dao;

    private User user;

    private ChatController chatController;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imgUtil = new ImgUtil();
        dateUtil = new DateUtil();
        dao = new UserDaoImpl();
    }

    public void initInfo(ChatController controller) throws IOException {
        chatController = controller;
        this.user = chatController.getUser();
        headImg.setImage(imgUtil.base64toImage(user.getHeadImg()));
        this.userName.setText(user.getUserName());
        pwd.setText(user.getPassword());
        if (user.getSex().equals("male"))
            selectSex("male");
        else
            selectSex("female");
        birthday.setValue(dateUtil.date2LocalDate(user.getBirthday()));
        motto.setText(user.getMotto());
        changeImg.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY){
                    File file = imgUtil.selectImage(changeImg.getScene());
                    if (file == null)
                        return ;
                    user.setHeadImg(imgUtil.imageToBase64(file));
                    try {
                        headImg.setImage(imgUtil.base64toImage(user.getHeadImg()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }

    public void maleButtonAction(ActionEvent actionEvent) {
        selectSex("male");
    }

    public void femaleButtonAction(ActionEvent actionEvent) {
        selectSex("female");
    }

    public void change_infoButtonAction(ActionEvent actionEvent) throws IOException {
        String userName = this.userName.getText();
        String password = this.pwd.getText();
        String sex = maleButton.isSelected() ? "male" : "female";
        Date birthday = this.birthday.getValue() != null ? dateUtil.localDate2Date(this.birthday.getValue()) : null;
        String motto = this.motto.getText();
        String headImg = user.getHeadImg();
        //  信息不完整
        if (userName.equals("") || password.equals("")
                || sex.equals("") || birthday == null || motto.equals("")){
            Stage stage = new Stage();
            Label l = new Label("信息不能为空!");
            Scene s = new Scene(l,200,124);
            stage.setScene(s);
            stage.show();
            return;
        }
        User usr = new User(user.getUserId(),user.getUserName(),password,sex,motto,birthday,user.getOnline(),headImg);
        dao.changeInfo(usr);
        Stage stage = new Stage();
        Label l = new Label("提交成功!");
        Scene s = new Scene(l,200,124);
        stage.setScene(s);
        stage.show();
        chatController.initChatBoxList(usr);
    }

    private void selectSex(String sex){
        if (sex.equals("male")){
            maleButton.setSelected(true);
            femaleButton.setSelected(false);
        }
        else{
            maleButton.setSelected(false);
            femaleButton.setSelected(true);
        }
    }
}
