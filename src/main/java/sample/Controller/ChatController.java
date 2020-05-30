package sample.Controller;

import com.sun.javafx.robot.FXRobot;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import sample.Dao.UserDao;
import sample.Dao.UserDaoImpl;
import sample.Entity.Msg;
import sample.Entity.User;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.ResourceBundle;

public class ChatController implements Initializable {
    @FXML
    private Pane ChatPane;
    @FXML
    private Button bar_portrait;    //头像
    @FXML
    private Button bar_chat;        //聊天
    @FXML
    private Button bar_friend;      //好友
    @FXML
    private Button bar_set;         //设置
    @FXML
    private Pane group_bar_chatboxlist;//聊天栏
    @FXML
    private Pane group_bar_friend;  //好友栏
    @FXML
    private Pane group_bar_chatWindow;
    @FXML
    private FlowPane friendlist;
    @FXML
    private Label content_name;
    @FXML
    private Button send_msg;
    @FXML
    private Label info_name;
    @FXML
    private FlowPane chatboxlist;
    @FXML
    private FlowPane msgList;
    @FXML
    private ScrollPane info_pane_box;
    @FXML
    private TextArea txt_input;
    @FXML
    private Button touch_send;


    private UserDao dao;

    private String userName;

    private String friendName;

    private List<String> chatBoxList;

    private List<User> friendList;

    private List<Msg> msgs;

    private boolean chatWindowFlag;

    private boolean last = true;
    public void initialize(URL location, ResourceBundle resources) {
        dao = new UserDaoImpl();
        chatWindowFlag = false;
        chatboxlist.setPadding(new Insets(5));
        friendlist.setPadding(new Insets(5));
        VBox.setVgrow(chatboxlist, Priority.ALWAYS);
        //发消息之后置底
        info_pane_box.vvalueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (last){
                    info_pane_box.setVvalue(1.0);
                    last = false;
                }
            }
        });
    }

    public void initChatBoxList(final String userName){
        this.userName = userName;
        chatBoxList = dao.getAllChat(userName);
        addchatboxlist();
    }

    public void bar_friendAction(javafx.event.ActionEvent actionEvent) {
        group_bar_chatWindow.setVisible(false);
        group_bar_chatboxlist.setVisible(false);
        group_bar_friend.setVisible(true);
        Stage stage = (Stage) ChatPane.getScene().getWindow();
        String userName = stage.getTitle();
        friendList = dao.getAllFriends(userName);
        friendlist.getChildren().clear();
        for (final User friend : friendList){
            //  头像
//            Image headImg = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(member.getHead())));
//            ImageView head = new ImageView();
//            head.setImage(headImg);
//            head.setFitWidth(40);
//            head.setFitHeight(40);
            Label name = new Label(friend.getUserName());
            name.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    send_msg.setVisible(true);
                    content_name.setText(friend.getUserName() + "\n"
                            + friend.getAge() + "\n"
                            + friend.getSex() + "\n"
                            + friend.getMotto() );
                }
            });
            name.setTextFill(Color.rgb(0,0,0));
//            Label status = new Label(member.getStatus() ? "在线" : "离线");
//            status.setTextFill(Color.rgb(255, 255, 255));
            VBox info = new VBox(8, name);
            info.setPadding(new Insets(2, 50, 10, 8));
            friendlist.getChildren().add(new HBox(info));
        }
    }

    public void bar_chatAction(ActionEvent actionEvent) {
        group_bar_friend.setVisible(false);
        group_bar_chatboxlist.setVisible(true);
        if (chatWindowFlag)
            group_bar_chatWindow.setVisible(true);
    }

    public void send_msgAction(ActionEvent actionEvent){
        group_bar_chatWindow.setVisible(true);
        group_bar_chatboxlist.setVisible(true);
        group_bar_friend.setVisible(false);
        //截取前面的name
        int len = content_name.getText().length();
        for (int i = 0 ; i < len ; i ++)
            if (content_name.getText().charAt(i) == '\n')
                len = i;
        friendName = content_name.getText().substring(0,len);
        chatBoxList.remove(friendName);
        chatBoxList.add(0,friendName);
        addchatboxlist();
        //显示最上面的
        {
            chatWindowFlag = true;
            group_bar_chatWindow.setVisible(true);
            info_name.setText(friendName);
            //聊天内容
            msgs = dao.getMsg(userName,friendName);
            msgList.getChildren().clear();
            for (Msg msg : msgs)
                addMessageBox(msg);
        }

    }

    public void touch_sendAction(ActionEvent actionEvent){
        String txt = txt_input.getText();
        if (txt.equals(""))
            return;
        txt_input.setText("");

    }

    private void addchatboxlist(){
        //  先清空
        chatboxlist.getChildren().clear();
        //  再逐个添加
        for (final String friendName : chatBoxList){
            //  头像
//            Image headImg = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(member.getHead())));
//            ImageView head = new ImageView();
//            head.setImage(headImg);
//            head.setFitWidth(40);
//            head.setFitHeight(40);
            Label name = new Label(friendName);
            name.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    chatWindowFlag = true;
                    group_bar_chatWindow.setVisible(true);
                    info_name.setText(friendName);
                    //聊天内容
                    msgs = dao.getMsg(userName,friendName);
                    msgList.getChildren().clear();
                    for (Msg msg : msgs)
                        addMessageBox(msg);
                    for (int i = 0, len = msgs.size(); i < len ; i ++){
                        System.out.print(msgs.get(i).getSenderName() + "\t");
                        System.out.print(msgs.get(i).getMsg() + "\t");
                        System.out.print(msgs.get(i).getReceiverName() + "\t");
                        System.out.print(msgs.get(i).getTime() + "\n");
                    }
                }
            });
            name.setTextFill(Color.rgb(0,0,0));
//            Label status = new Label(member.getStatus() ? "在线" : "离线");
//            status.setTextFill(Color.rgb(255, 255, 255));
            VBox info = new VBox(8, name);
            info.setPadding(new Insets(2, 50, 10, 8));
            chatboxlist.getChildren().add(new HBox(info));
        }
    }

    private void addMessageBox(Msg message){
//        User sender = dao.getMemberById(message.getSenderId());
//        assert sender != null;
//        Image headImg = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(sender.getHead())));
//        ImageView head = new ImageView();
//        head.setImage(headImg);
//        head.setFitWidth(40);
//        head.setFitHeight(40);

        Label messageBubble = new Label(message.getMsg());
        messageBubble.setWrapText(true);
        messageBubble.setMaxWidth(220);
        messageBubble.setStyle("-fx-background-color: rgb(179,231,244); -fx-background-radius: 8px;");
        messageBubble.setPadding(new Insets(6));
        messageBubble.setFont(new Font(14));
        HBox.setMargin(messageBubble, new Insets(8, 0, 0, 0));

        boolean isMine = message.getSenderName().equals(userName);
        double[] points;
        if (!isMine) {
            points = new double[]{
                    0.0, 5.0,
                    10.0, 0.0,
                    10.0, 10.0
            };
        } else {
            points = new double[]{
                    0.0, 0.0,
                    0.0, 10.0,
                    10.0, 5.0
            };
        }
        Polygon triangle = new Polygon(points);
        triangle.setFill(Color.rgb(179,231,244));
        HBox messageBox = new HBox();
        messageBox.setPrefWidth(650);
        messageBox.setPadding(new Insets(10, 5, 10, 5));
        if (isMine) {
            HBox.setMargin(triangle, new Insets(15, 0, 0, 0));
           // messageBox.getChildren().addAll(messageBubble, triangle,head);
            messageBox.getChildren().addAll(messageBubble,triangle);
            messageBox.setAlignment(Pos.TOP_RIGHT);
        } else {
            HBox.setMargin(triangle, new Insets(15, 0, 0, 32));
            messageBox.getChildren().addAll(triangle,messageBubble);
            //messageBox.getChildren().addAll(head, triangle, messageBubble);
        }

        last = info_pane_box.getVvalue() == 1.0;
        msgList.getChildren().add(messageBox);
    }
}