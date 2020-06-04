package sample.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import sample.Dao.UserDao;
import sample.Dao.UserDaoImpl;
import sample.Entity.User;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    Pane loginPane;
    @FXML
    private TextField Name;
    @FXML
    private TextField Password;

    private UserDao dao;
    public void initialize(URL location, ResourceBundle resources) {
        dao = new UserDaoImpl();
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
        //登录成功
        else{
            System.out.println("Login!");
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
            stage.show();
            ChatController controller = (ChatController)fxmlLoader.getController();
            controller.initChatBoxList(user);
        }
    }

    public void register_buttonAction(ActionEvent actionEvent) {
    }
}
