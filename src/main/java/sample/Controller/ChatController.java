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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
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
    @FXML
    private TextField chat_search;
    @FXML
    private Label chat_searchAdd;
    @FXML
    private TextField friend_search;
    @FXML
    private Label friend_searchAdd;

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
        loadchatboxlist();
        chat_searchAdd.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY){
                    chatboxlist.getChildren().clear();
                    for (int i = 0, len = chatBoxList.size() ; i < len ; i ++)
                        if (chatBoxList.get(i).equals(chat_search.getText()))
                            addchatboxlist(chatBoxList.get(i));
                }
            }
        });
        friend_searchAdd.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY){
                    friendlist.getChildren().clear();
                    for (int i = 0, len = friendList.size() ; i < len ; i ++)
                        if (friendList.get(i).getUserName().equals(friend_search.getText()))
                            addfriendlist(friendList.get(i));
                }
            }
        });
    }
    //好友栏
    public void bar_friendAction(javafx.event.ActionEvent actionEvent) {
        friend_search.setText("");
        group_bar_chatWindow.setVisible(false);
        group_bar_chatboxlist.setVisible(false);
        group_bar_friend.setVisible(true);
        friendList = dao.getAllFriends(userName);
        friendlist.getChildren().clear();
        for (final User friend : friendList)
            addfriendlist(friend);
    }
    //  聊天栏
    public void bar_chatAction(ActionEvent actionEvent) {
        chat_search.setText("");
        group_bar_friend.setVisible(false);
        group_bar_chatboxlist.setVisible(true);
        if (chatWindowFlag)
            group_bar_chatWindow.setVisible(true);
        if (group_bar_chatWindow.isVisible())
            addChatWindow(friendName);
        else if (group_bar_chatboxlist.isVisible()){
            chatBoxList = dao.getAllChat(userName);
            loadchatboxlist();
        }
    }
    //  好友信息界面的发送消息
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
    //  聊天框的发送按钮
    public void touch_sendAction(ActionEvent actionEvent){
        String txt = txt_input.getText();
        if (txt.equals(""))
            return;
        txt_input.setText("");
        Msg msg = new Msg(userName,friendName,txt,new Date(),"person");
        dao.sendMsg(msg);
        addMessageBox(msg);
        chatBoxList.remove(friendName);
        chatBoxList.add(0,friendName);
        //显示最上面的
        addChatWindow(friendName);
    }
    //  加载聊天栏
    private void loadchatboxlist(){
        //  先清空
        chatboxlist.getChildren().clear();
        //  再逐个添加
        for (final String friendname : chatBoxList)
            addchatboxlist(friendname);
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
        loadchatboxlist();
    }
    //  聊天框中添加一条消息
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
    //  聊天列表添加一个聊天
    private void addchatboxlist(final String friendname){
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
                chat_search.setText("");
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
    //  好友列表添加一个好友
    private void addfriendlist(final User friend){
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
                friend_search.setText("");
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
