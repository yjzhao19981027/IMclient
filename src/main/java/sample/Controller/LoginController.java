package sample.Controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.Dao.UserDao;
import sample.Dao.UserDaoImpl;
import sample.Entity.User;
import sample.Util.ChatWindowUtil;
import sample.Util.Storage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    Pane loginPane;
    @FXML
    private TextField Name;
    @FXML
    private TextField Password;
    @FXML
    private Label register;

    private UserDao dao;
    public void initialize(URL location, ResourceBundle resources) {
        dao = new UserDaoImpl();
        Storage.loginController = LoginController.this;
        register.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Register!");
                Stage primaryStage = new Stage();
                URL path = getClass().getResource("/FXML/Register/register.fxml");
                Parent root = null;
                try {
                    root = FXMLLoader.load(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ChatWindowUtil.setDragged(root, primaryStage);
                primaryStage.setTitle("注册");
                primaryStage.setScene(new Scene(root, 604, 515));
                primaryStage.initStyle(StageStyle.UNDECORATED);
                primaryStage.show();
            }
        });
    }

    //  最小化按钮
    public void minAction(ActionEvent actionEvent){
        Stage stage = (Stage) loginPane.getScene().getWindow();
        stage.setIconified(true);
    }

    public void closeAction(ActionEvent actionEvent){
        System.exit(0);
    }

    public void login_buttonAction(ActionEvent actionEvent) throws Exception {
        String userName = Name.getText();
        String password = Password.getText();
        //用户名密码框为空
        if (userName.equals("") || password.equals("")){
            Stage stage = new Stage();
            Label l = new Label("用户名和密码格式错误!");
            Scene s = new Scene(l,200,124);
            stage.setScene(s);
            stage.show();
            return;
        }
        //数据库查询
        User user = dao.getUserByUserName_Password(userName,password);
        //数据库中不存在
        if (user == null){
            Stage stage = new Stage();
            Label l = new Label("用户名或密码错误!");
            Scene s = new Scene(l,200,124);
            stage.setScene(s);
            stage.show();
        }
        else if (user.getOnline()){
            Stage stage = new Stage();
            Label l = new Label("用户已在线!");
            Scene s = new Scene(l,200,124);
            stage.setScene(s);
            stage.show();
        }
        //登录成功
        else{
            System.out.println("Login!");
            Storage.channel.writeAndFlush("login " + userName + "\r\n");
            Stage stage = (Stage) loginPane.getScene().getWindow();
            stage.close();
            stage = new Stage();
            URL path = getClass().getResource("/FXML/Chat/chat.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(path);
            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
            Parent root = fxmlLoader.load();
            stage.setTitle(userName);
            stage.setScene(new Scene(root, 994.4, 656));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
            ChatWindowUtil.setDragged(root, stage);

            ChatController controller = (ChatController)fxmlLoader.getController();
            Storage.user = user;
            controller.initChatBoxList();
        }
    }
}
