package sample.Controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import sample.Dao.UserDao;
import sample.Dao.UserDaoImpl;
import sample.Entity.Msg;
import sample.Entity.User;

import java.net.URL;
import java.util.*;

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

    //LoginController使用，将userName过渡
    public void initChatBoxList(final String userName){
        this.userName = userName;
        chatBoxList = dao.getAllChat(userName);
        addchatboxlist();
    }
    //好友栏
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
    //聊天栏
    public void bar_chatAction(ActionEvent actionEvent) {
        group_bar_friend.setVisible(false);
        group_bar_chatboxlist.setVisible(true);
        if (chatWindowFlag)
            group_bar_chatWindow.setVisible(true);
    }
    //好友信息界面的发送消息
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
        //显示最上面的
        addChatWindow(friendName);
    }
    //聊天框的发送按钮
    public void touch_sendAction(ActionEvent actionEvent){
        String txt = txt_input.getText();
        if (txt.equals(""))
            return;
        txt_input.setText("");
        Msg msg = new Msg(userName,friendName,txt,new Date(),"person");
        dao.sendMsg(msg);
        addMessageBox(msg);
    }
    //加载聊天栏
    private void addchatboxlist(){
        //  先清空
        chatboxlist.getChildren().clear();
        //  再逐个添加
        for (final String friendname : chatBoxList){
            //  头像
//            Image headImg = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(member.getHead())));
//            ImageView head = new ImageView();
//            head.setImage(headImg);
//            head.setFitWidth(40);
//            head.setFitHeight(40);
            int num = dao.getUnreadMsgNum(friendname,userName);
            Label name = new Label(friendname);
            Label unread = new Label(String.valueOf(num) + "条未读消息");
            name.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    addChatWindow(friendname);
                }
            });
            name.setTextFill(Color.rgb(0,0,0));
            unread.setTextFill(Color.rgb(0,0,0));
//            Label status = new Label(member.getStatus() ? "在线" : "离线");
//            status.setTextFill(Color.rgb(255, 255, 255));
            VBox info = new VBox(8, name);
            info.setPadding(new Insets(2, 50, 10, 8));
            if (num == 0)
                chatboxlist.getChildren().add(new HBox(info));
            else
                chatboxlist.getChildren().add(new HBox(info,unread));
        }
    }
    //聊天框添加消息
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

    private void addChatWindow(String friendname){
        chatWindowFlag = true;
        group_bar_chatWindow.setVisible(true);
        info_name.setText(friendname);
        friendName = friendname;
        //聊天内容
        dao.setMsgIsRead(friendname,userName);
        msgs = dao.getMsg(userName,friendname);
        msgList.getChildren().clear();
        for (Msg msg : msgs)
            addMessageBox(msg);
        addchatboxlist();
    }
}
