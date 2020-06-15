package sample.Controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import sample.Dao.UserDao;
import sample.Dao.UserDaoImpl;
import sample.Entity.Msg;
import sample.Entity.User;
import sample.Util.ChatWindowUtil;
import sample.Util.ImgUtil;
import sample.Util.Storage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ChatController implements Initializable {
    @FXML
    private Pane ChatPane;
    @FXML
    private ImageView bar_headImg;    //头像
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

    private Parent root;

    private UserDao dao;

    private String friendName;

    private List<String> chatBoxList;   //聊天框名称

    private List<User> friendList;

    private List<Msg> msgs;

    private ImgUtil util;

    private ChatWindowUtil chatWindowUtil;

    private boolean chatWindowFlag;

    private boolean last = true;

    public void initialize(URL location, ResourceBundle resources) {
        Storage.chatController = ChatController.this;
        dao = new UserDaoImpl();
        util = new ImgUtil();
        chatWindowUtil = new ChatWindowUtil();
        chatWindowFlag = false;
        chatboxlist.setPadding(new Insets(5));
        friendlist.setPadding(new Insets(5));
        VBox.setVgrow(chatboxlist, Priority.ALWAYS);
        //发消息之后置底
        info_pane_box.vvalueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (last) {
                    info_pane_box.setVvalue(1.0);
                    last = false;
                }
            }
        });
    }

    //  LoginController使用，将userName过渡
    public void initChatBoxList() throws IOException {
        bar_headImg.setImage(util.base64toImage(Storage.user.getHeadImg()));
        bar_headImg.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    Stage primaryStage = new Stage();
                    URL path = getClass().getResource("/FXML/Info/information.fxml");
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(path);
                    fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
                    Parent root = null;
                    try {
                        root = fxmlLoader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    primaryStage.setTitle("个人信息");
                    primaryStage.setScene(new Scene(root, 540, 480));
                    primaryStage.show();
                    InfoController controller = (InfoController) fxmlLoader.getController();
                    try {
                        controller.initInfo(ChatController.this);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        chatBoxList = dao.getAllChat(Storage.user.getUserName());
        loadChatBoxList();
        //  聊天栏搜索按钮
        chat_searchAdd.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    chatboxlist.getChildren().clear();
                    for (int i = 0, len = chatBoxList.size(); i < len; i++)
                        if (chatBoxList.get(i).equals(chat_search.getText())) {
                            try {
                                add2ChatBoxList(chatBoxList.get(i));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                }
            }
        });
        //  好友栏搜索按钮
        friend_searchAdd.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    friendlist.getChildren().clear();
                    for (int i = 0, len = friendList.size(); i < len; i++)
                        if (friendList.get(i).getUserName().equals(friend_search.getText())) {
                            try {
                                add2FriendList(friendList.get(i));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                }
            }
        });
    }

    //  好友栏
    public void bar_friendAction(javafx.event.ActionEvent actionEvent) throws IOException {
        friend_search.setText("");
        group_bar_chatWindow.setVisible(false);
        group_bar_chatboxlist.setVisible(false);
        group_bar_friend.setVisible(true);
        friendList = dao.getAllFriends(Storage.user.getUserName());
        friendlist.getChildren().clear();
        for (final User friend : friendList)
            add2FriendList(friend);
    }

    //  聊天栏
    public void bar_chatAction(ActionEvent actionEvent) throws IOException {
        chat_search.setText("");
        group_bar_friend.setVisible(false);
        group_bar_chatboxlist.setVisible(true);
        if (chatWindowFlag)
            group_bar_chatWindow.setVisible(true);
        if (group_bar_chatWindow.isVisible())
            loadChatWindow(friendName);
        else if (group_bar_chatboxlist.isVisible()) {
            chatBoxList = dao.getAllChat(Storage.user.getUserName());
            loadChatBoxList();
        }
    }

    //  好友信息界面的发送消息
    public void send_msgAction(ActionEvent actionEvent) throws IOException {
        group_bar_chatWindow.setVisible(true);
        group_bar_chatboxlist.setVisible(true);
        group_bar_friend.setVisible(false);
        //截取前面的name
        int len = content_name.getText().length();
        for (int i = 0; i < len; i++)
            if (content_name.getText().charAt(i) == '\n')
                len = i;
        friendName = content_name.getText().substring(0, len);
        chatBoxList.remove(friendName);
        chatBoxList.add(0, friendName);
        //显示最上面的
        loadChatWindow(friendName);
    }

    //  聊天框的发送按钮
    public void touch_sendAction(ActionEvent actionEvent) throws IOException {
        String txt = txt_input.getText();
        if (txt.equals(""))
            return;
        txt_input.setText("");
        Msg msg = new Msg(Storage.user.getUserName(), friendName, txt, null, new Date(), "person");
        dao.sendMsg(msg);
        add2ChatBox(msg);
        chatBoxList.remove(friendName);
        chatBoxList.add(0, friendName);
        //显示最上面的
        loadChatWindow(friendName);
        Storage.channel.writeAndFlush("sendMsg " + friendName + "\r\n");
    }

    //  发送图片按钮
    public void send_imgAction(ActionEvent actionEvent) throws IOException {
        System.out.println("send_img");
        File file = util.selectImage(group_bar_chatboxlist.getScene());
        if (file == null)
            return;
        String img = util.imageToBase64(file);
        Msg msg = new Msg(Storage.user.getUserName(), friendName, null, img, new Date(), "person");
        dao.sendMsg(msg);
        add2ChatBox(msg);
        loadChatWindow(friendName);
        Storage.channel.writeAndFlush("sendMsg " + friendName + "\r\n");
    }

    //  加载聊天栏
    private void loadChatBoxList() throws IOException {
        //  先清空
        chatboxlist.getChildren().clear();
        //  再逐个添加
        for (final String friendname : chatBoxList)
            add2ChatBoxList(friendname);
    }

    //  加载聊天框
    private void loadChatWindow(String friendname) throws IOException {
        chatWindowFlag = true;
        group_bar_chatWindow.setVisible(true);
        info_name.setText(friendname);
        friendName = friendname;
        //聊天内容
        dao.setMsgIsRead(friendname, Storage.user.getUserName());
        msgs = dao.getMsg(Storage.user.getUserName(), friendname);
        //添加聊天消息
        msgList.getChildren().clear();
        for (Msg msg : msgs)
            add2ChatBox(msg);
        //重新加载聊天栏
        loadChatBoxList();
    }

    //  聊天框中添加一条消息(包含图片)
    private void add2ChatBox(Msg message) throws IOException {
        String headstr = dao.getHeadByUserName(message.getSenderName());
        Image headImg = util.base64toImage(headstr);
        ImageView head = new ImageView();
        head.setImage(headImg);
        head.setFitWidth(40);
        head.setFitHeight(40);

        Label messageBubble = new Label(message.getMsg());
        ImageView img = new ImageView();
        ;
        if (message.getImg() == null) {
            messageBubble.setWrapText(true);
            messageBubble.setMaxWidth(220);
            messageBubble.setStyle("-fx-background-color: rgb(179,231,244); -fx-background-radius: 8px;");
            messageBubble.setPadding(new Insets(6));
            messageBubble.setFont(new Font(14));
            HBox.setMargin(messageBubble, new Insets(8, 0, 0, 0));
        } else {
            Image image = util.base64toImage(message.getImg());
            img.setImage(image);
            img.setFitWidth(300);
            img.setFitHeight(300);
        }

        boolean isMine = message.getSenderName().equals(Storage.user.getUserName());
        double[] points = chatWindowUtil.setPoints(isMine);

        Polygon triangle = new Polygon(points);
        triangle.setFill(Color.rgb(179, 231, 244));
        HBox messageBox = new HBox();
        messageBox.setPrefWidth(650);
        messageBox.setPadding(new Insets(10, 5, 10, 5));
        if (isMine) {
            HBox.setMargin(triangle, new Insets(15, 0, 0, 0));
            if (message.getImg() == null)
                messageBox.getChildren().addAll(messageBubble, triangle, head);
            else
                messageBox.getChildren().addAll(img, triangle, head);
            messageBox.setAlignment(Pos.TOP_RIGHT);
        } else {
            HBox.setMargin(triangle, new Insets(15, 0, 0, 32));
            if (message.getImg() == null)
                messageBox.getChildren().addAll(head, triangle, messageBubble);
            else
                messageBox.getChildren().addAll(head, triangle, img);
        }

        last = info_pane_box.getVvalue() == 1.0;
        msgList.getChildren().add(messageBox);
    }

    //  聊天栏添加一个聊天
    private void add2ChatBoxList(final String friendname) throws IOException {
        //  头像
        String img = dao.getHeadByUserName(friendname);
        Image headImg = util.base64toImage(img);
        ImageView head = new ImageView();
        head.setImage(headImg);
        head.setFitWidth(40);
        head.setFitHeight(40);
        int num = dao.getUnreadMsgNum(friendname, Storage.user.getUserName());
        Label name = new Label(friendname);
        Label unread = new Label(String.valueOf(num) + "条未读消息");
        name.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                chat_search.setText("");
                try {
                    loadChatWindow(friendname);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        name.setTextFill(Color.rgb(0, 0, 0));
        unread.setTextFill(Color.rgb(0, 0, 0));
        VBox info = new VBox(8, name);
        info.setPadding(new Insets(2, 50, 10, 8));
        if (num == 0)
            chatboxlist.getChildren().add(new HBox(head, info));
        else
            chatboxlist.getChildren().add(new HBox(head, info, unread));
    }

    //  好友列表添加一个好友
    private void add2FriendList(final User friend) throws IOException {
        //  头像
        Image headImg = util.base64toImage(friend.getHeadImg());
        ImageView head = new ImageView();
        head.setImage(headImg);
        head.setFitWidth(40);
        head.setFitHeight(40);

        Label name = new Label(friend.getUserName());
        name.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                friend_search.setText("");
                send_msg.setVisible(true);
                content_name.setText(friend.getUserName() + "\n"
                        + friend.getBirthday() + "\n"
                        + friend.getSex() + "\n"
                        + friend.getMotto());
            }
        });
        name.setTextFill(Color.rgb(0, 0, 0));
        VBox info = new VBox(8, name);
        info.setPadding(new Insets(2, 50, 10, 8));
        friendlist.getChildren().add(new HBox(head, info));
    }

    public void refreshen() throws IOException {
        if (group_bar_chatWindow.isVisible())
            loadChatWindow(friendName);
        else if (group_bar_chatboxlist.isVisible()) {
            chatBoxList = dao.getAllChat(Storage.user.getUserName());
            loadChatBoxList();
        }
    }
}
