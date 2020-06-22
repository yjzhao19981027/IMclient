package sample.Controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sample.Dao.UserDao;
import sample.Dao.UserDaoImpl;
import sample.Entity.User;
import sample.Util.DateUtil;
import sample.Util.ImgUtil;
import sample.Util.Storage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
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
    private TextArea motto;


    private UserDao dao;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dao = new UserDaoImpl();
    }

    public void initInfo(ChatController controller) throws IOException {
        headImg.setImage(ImgUtil.base64toImage(Storage.user.getHeadImg()));
        this.userName.setText(Storage.user.getUserName());
        pwd.setText(Storage.user.getPassword());
        if (Storage.user.getSex().equals("male"))
            selectSex("male");
        else
            selectSex("female");
        birthday.setValue(DateUtil.date2LocalDate(Storage.user.getBirthday()));
        motto.setText(Storage.user.getMotto());
        changeImg.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY){
                    File file = ImgUtil.selectImage(changeImg.getScene());
                    if (file == null)
                        return ;
                    Storage.user.setHeadImg(ImgUtil.imageToBase64(file));
                    try {
                        headImg.setImage(ImgUtil.base64toImage(Storage.user.getHeadImg()));
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
        Date birthday = this.birthday.getValue() != null ? DateUtil.localDate2Date(this.birthday.getValue()) : null;
        String motto = this.motto.getText();
        String headImg = Storage.user.getHeadImg();
        //  信息不完整
        if (userName.equals("") || password.equals("") || birthday == null || motto.equals("")){
            Stage stage = new Stage();
            Label l = new Label("信息不能为空!");
            Scene s = new Scene(l,200,124);
            stage.setScene(s);
            stage.show();
            return;
        }
        User usr = new User(Storage.user.getUserId(),Storage.user.getUserName(),password,sex,motto,birthday,Storage.user.getOnline(),headImg);
        dao.changeInfo(usr);
        Stage stage = new Stage();
        Label l = new Label("提交成功!");
        Scene s = new Scene(l,200,124);
        stage.setScene(s);
        stage.show();
        Storage.chatController.initChatBoxList();
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
