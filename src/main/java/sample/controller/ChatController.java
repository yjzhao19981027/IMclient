package sample.controller;

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
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.dao.Dao;
import sample.dao.MsgDaoImpl;
import sample.dao.UserDaoImpl;
import sample.entity.Msg;
import sample.entity.User;
import sample.util.ChatWindowUtil;
import sample.util.ImgUtil;
import sample.util.Storage;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class ChatController implements Initializable {
    @FXML
    private Pane ChatPane;
    @FXML
    private Pane face;
    @FXML
    private ImageView bar_headImg;    //头像
    @FXML
    private Pane group_bar_chatboxlist;//聊天栏
    @FXML
    private Pane group_bar_friend;  //好友栏
    @FXML
    private Pane content;
    @FXML
    private Pane group_bar_chatWindow;
    @FXML
    private FlowPane friendlist;
    @FXML
    private Label content_name;
    @FXML
    private ImageView content_headImg;
    @FXML
    private Label content_age;
    @FXML
    private Label content_sex;
    @FXML
    private Label content_motto;
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
    private TextField chat_search;
    @FXML
    private Label chat_searchAdd;
    @FXML
    private TextField friend_search;
    @FXML
    private Label friend_searchAdd;

    private Dao usrDao;
    private Dao msgDao;

    private String friendName;

    private List<String> chatBoxList;   //聊天框名称

    private List<User> friendList;

    private List<Msg> msgs;

    private ChatWindowUtil chatWindowUtil;

    private boolean chatWindowFlag;

    private boolean last = true;

    public void initialize(URL location, ResourceBundle resources) {
        Storage.chatController = ChatController.this;
        usrDao = new UserDaoImpl();
        msgDao = new MsgDaoImpl();
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
        bar_headImg.setImage(ImgUtil.base64toImage(Storage.user.getHeadImg()));
        bar_headImg.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    Stage primaryStage = new Stage();
                    URL path = getClass().getResource("/fxml/info/information.fxml");
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
                    primaryStage.setScene(new Scene(root, 611, 412));
                    primaryStage.initStyle(StageStyle.UNDECORATED);
                    primaryStage.show();
                    ChatWindowUtil.setDragged(root, primaryStage);
                    InfoController controller = (InfoController) fxmlLoader.getController();
                    try {
                        controller.initInfo(ChatController.this);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        chatBoxList = usrDao.getAllChat(Storage.user.getUserName());
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

    //  最小化按钮
    public void minAction(ActionEvent actionEvent){
        Stage stage = (Stage) ChatPane.getScene().getWindow();
        stage.setIconified(true);
    }

    //  好友栏按钮
    public void bar_friendAction(javafx.event.ActionEvent actionEvent) throws IOException {
        friend_search.setText("");
        group_bar_chatWindow.setVisible(false);
        group_bar_chatboxlist.setVisible(false);
        group_bar_friend.setVisible(true);
        friendList = usrDao.getAllFriends(Storage.user.getUserName());
        loadFriendBar();
    }

    //  聊天栏按钮
    public void bar_chatAction(ActionEvent actionEvent) throws IOException {
        chat_search.setText("");
        group_bar_friend.setVisible(false);
        group_bar_chatboxlist.setVisible(true);
        if (chatWindowFlag)
            group_bar_chatWindow.setVisible(true);
        chatBoxList = usrDao.getAllChat(Storage.user.getUserName());
        if (group_bar_chatWindow.isVisible() && chatBoxList.contains(friendName))
            loadChatWindow(friendName);
        else if(group_bar_chatWindow.isVisible() && !chatBoxList.contains(friendName)){
            chatWindowFlag = false;
            group_bar_chatWindow.setVisible(false);
        }
        loadChatBoxList();
    }

    //  添加好友按钮
    public void bar_addFriendAction(ActionEvent actionEvent) throws IOException {
        Stage primaryStage = new Stage();
        URL path = getClass().getResource("/fxml/addfriend/addFriend.fxml");
        Parent root = FXMLLoader.load(path);
        primaryStage.setTitle("添加好友");
        primaryStage.setScene(new Scene(root, 405, 496));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
        ChatWindowUtil.setDragged(root, primaryStage);
    }

    //  注销按钮
    public void bar_logoutAction(ActionEvent actionEvent){
        Storage.channel.writeAndFlush("logout " + Storage.user.getUserName() + "\r\n");
        System.exit(0);
    }

    //  好友信息界面的发送消息
    public void send_msgAction(ActionEvent actionEvent) throws IOException {
        group_bar_chatWindow.setVisible(true);
        group_bar_chatboxlist.setVisible(true);
        group_bar_friend.setVisible(false);
        //截取前面的name
        friendName = content_name.getText().substring(4);
        if (chatBoxList.contains(friendName))
            chatBoxList.remove(friendName);
        chatBoxList.add(0, friendName);
        //显示最上面的
        loadChatWindow(friendName);
        loadChatBoxList();
    }

    //  聊天框的发送按钮
    public void touch_sendAction(ActionEvent actionEvent) throws IOException {
        String txt = txt_input.getText();
        if (txt.equals(""))
            return;
        txt_input.setText("");
        Msg msg = new Msg(Storage.user.getUserName(), friendName, txt, null, new Date(), "text");
        msgDao.sendMsg(msg);
        //  添加到聊天框
        add2ChatBox(msg);
        chatBoxList.remove(friendName);
        chatBoxList.add(0, friendName);
        //  置顶聊天
        loadChatWindow(friendName);
        loadChatBoxList();
        //  借助服务器通知另一端接收
        Storage.channel.writeAndFlush("sendMsg " + friendName + "\r\n");
    }

    //  发送表情包按钮
    public void faceAction(ActionEvent actionEvent) throws URISyntaxException {
        if (face.isVisible()){
            face.setVisible(false);
            return;
        }
        face.setVisible(true);
        List<Object> facelist = new ArrayList<>();
        VBox box = new VBox();
        for (int j = 0 ; j < 7 ; j ++){
            HBox vBox = new HBox();
            for (int i = 0 ; i < 10 ; i ++){
                String url = "fxml/img/face/emoji_" + String.valueOf(j) + String.valueOf(i) + ".png";
                ImageView imageView = new ImageView();
                Image image = new Image(url);
                imageView.setImage(image);
                imageView.setFitHeight(20);
                imageView.setFitWidth(20);
                imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        File file = null;
                        try {
                            file = new File(this.getClass().getResource("/").toURI().getPath() + url);
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                        String img = ImgUtil.imageToBase64(file);
                        Msg msg = new Msg(Storage.user.getUserName(), friendName, null, img, new Date(), "face");
                        msgDao.sendMsg(msg);
                        try {
                            add2ChatBox(msg);
                            loadChatWindow(friendName);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Storage.channel.writeAndFlush("sendMsg " + friendName + "\r\n");
                        face.setVisible(false);
                    }
                });
                vBox.getChildren().add(imageView);
                vBox.setMargin(imageView,new Insets(5));
                if (j == 6 && i == 7)
                    break;
                //facelist.add(imageView);
            }
            box.getChildren().add(vBox);
        }
        face.getChildren().add(box);
//        ObservableList<Object> strList = FXCollections.observableArrayList(facelist);
//        faces.setItems(strList);
    }

    //  发送图片按钮
    public void send_imgAction(ActionEvent actionEvent) throws IOException {
        System.out.println("send_img");
        File file = ImgUtil.selectImage(group_bar_chatboxlist.getScene());
        if (file == null)
            return;
        String img = ImgUtil.imageToBase64(file);
        Msg msg = new Msg(Storage.user.getUserName(), friendName, null, img, new Date(), "img");
        msgDao.sendMsg(msg);
        add2ChatBox(msg);
        loadChatWindow(friendName);
        Storage.channel.writeAndFlush("sendMsg " + friendName + "\r\n");
    }

    //  语音通话按钮
    public void callAction(ActionEvent actionEvent) throws IOException, InterruptedException {
        Storage.channel.writeAndFlush("call " + friendName + "\r\n");
        Storage.callFriend = friendName;
    }

    //  加载好友栏
    private void loadFriendBar() throws IOException {
        friendlist.getChildren().clear();
        for (final User friend : friendList)
            add2FriendList(friend);
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
        msgDao.setMsgIsRead(friendname, Storage.user.getUserName());
        msgs = msgDao.getMsg(Storage.user.getUserName(), friendname);
        //添加聊天消息
        msgList.getChildren().clear();
        for (Msg msg : msgs)
            add2ChatBox(msg);
    }

    //  聊天框中添加一条消息(包含图片)
    private void add2ChatBox(Msg message) throws IOException {
        String headstr = usrDao.getHeadByUserName(message.getSenderName());
        Image headImg = ImgUtil.base64toImage(headstr);
        ImageView head = new ImageView();
        head.setImage(headImg);
        head.setFitWidth(40);
        head.setFitHeight(40);

        Label messageBubble = new Label(message.getMsg());
        ImageView img = new ImageView();

        if (message.getMsgType().equals("text")) {
            messageBubble.setWrapText(true);
            messageBubble.setMaxWidth(220);
            messageBubble.setStyle("-fx-background-color: rgb(179,231,244); -fx-background-radius: 8px;");
            messageBubble.setPadding(new Insets(6));
            messageBubble.setFont(new Font(14));
            HBox.setMargin(messageBubble, new Insets(8, 0, 0, 0));
        }
        else if (message.getMsgType().equals("img")){
            Image image = ImgUtil.base64toImage(message.getImg());
            img.setImage(image);
            img.setFitWidth(300);
            img.setFitHeight(300);
        }
        else{
            Image image = ImgUtil.base64toImage(message.getImg());
            img.setImage(image);
            img.setFitWidth(30);
            img.setFitHeight(30);
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
        String img = usrDao.getHeadByUserName(friendname);
        Image headImg = ImgUtil.base64toImage(img);
        ImageView head = new ImageView();
        head.setImage(headImg);
        head.setFitWidth(40);
        head.setFitHeight(40);
        int num = msgDao.getUnreadMsgNum(friendname, Storage.user.getUserName());
        Label name = new Label(friendname);
        Label unread = new Label(String.valueOf(num) + "条未读消息");
        name.setTextFill(Color.rgb(0, 0, 0));
        unread.setTextFill(Color.rgb(0, 0, 0));
        VBox info = new VBox(8, name);
        info.setPadding(new Insets(2, 50, 10, 8));
        //  右单击菜单
        ContextMenu contextMenu = new ContextMenu();
        MenuItem item1 = new MenuItem("置顶聊天");
        item1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                chatBoxList.remove(friendname);
                chatBoxList.add(0, friendname);
                try {
                    loadChatBoxList();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        MenuItem item2 = new MenuItem("删除聊天");
        item2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //删除所有聊天消息
                msgDao.delChatMsg(Storage.user.getUserName(),friendname);
                msgDao.delChatMsg(friendname,Storage.user.getUserName());
                //删除聊天框
                chatBoxList.remove(friendname);
                try {
                    loadChatBoxList();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                group_bar_chatWindow.setVisible(false);
                chatWindowFlag = false;
            }
        });
        contextMenu.getItems().addAll(item1,item2);
        HBox together;
        if (num == 0)
            together = new HBox(head, info);
        else
            together = new HBox(head, info, unread);
        together.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY){
                    chat_search.setText("");
                    try {
                        loadChatWindow(friendname);
                        loadChatBoxList();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if (event.getButton() == MouseButton.SECONDARY)
                    contextMenu.show(together, event.getScreenX(), event.getScreenY());
            }
        });
        chatboxlist.getChildren().add(together);
    }

    //  好友列表添加一个好友
    private void add2FriendList(final User friend) throws IOException {
        //  头像
        Image headImg = ImgUtil.base64toImage(friend.getHeadImg());
        ImageView head = new ImageView();
        head.setImage(headImg);
        head.setFitWidth(40);
        head.setFitHeight(40);

        Label name = new Label(friend.getUserName());
        name.setTextFill(Color.rgb(0, 0, 0));
        VBox info = new VBox(8, name);
        info.setPadding(new Insets(2, 50, 10, 8));
        HBox together = new HBox(head,info);
        //  右单击菜单
        ContextMenu contextMenu = new ContextMenu();
        MenuItem item1 = new MenuItem("发送消息");
        item1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                group_bar_chatWindow.setVisible(true);
                group_bar_chatboxlist.setVisible(true);
                group_bar_friend.setVisible(false);
                //截取前面的name
                String friendName = friend.getUserName();
                if (chatBoxList.contains(friendName))
                    chatBoxList.remove(friendName);
                chatBoxList.add(0, friendName);
                //显示最上面的
                try {
                    loadChatBoxList();
                    loadChatWindow(friendName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        MenuItem item2 = new MenuItem("删除好友");
        item2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String friendName = friend.getUserName();
                content.setVisible(false);
                //删除好友
                usrDao.delFriend(Storage.user.getUserName(),friendName);
                usrDao.delFriend(friendName,Storage.user.getUserName());
                //删除所有聊天消息
                msgDao.delChatMsg(Storage.user.getUserName(),friendName);
                msgDao.delChatMsg(friendName,Storage.user.getUserName());
                //删除聊天框
                chatBoxList.remove(friendName);
                if (friend.getUserName().equals(friendName)){
                    group_bar_chatWindow.setVisible(false);
                    chatWindowFlag = false;
                }
                friendList.remove(friend);
                Storage.channel.writeAndFlush("delFriend " + friendName + "\r\n");
                try {
                    loadFriendBar();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        contextMenu.getItems().addAll(item1,item2);
        together.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    friend_search.setText("");
                    content.setVisible(true);
                    try {
                        content_headImg.setImage(ImgUtil.base64toImage(friend.getHeadImg()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    content_name.setText("用户名：" + friend.getUserName());
                    content_age.setText("年龄：" + String.valueOf(new Date().getYear() - friend.getBirthday().getYear()));
                    String sex = friend.getSex().equals("male") ? "男" : "女";
                    content_sex.setText("性别：" + sex);
                    content_motto.setText("个性签名：" + friend.getMotto());
                    content.setVisible(true);
                }
                else if (event.getButton() == MouseButton.SECONDARY)
                    contextMenu.show(together, event.getScreenX(), event.getScreenY());
            }
        });
        friendlist.getChildren().add(together);
    }

    public void refreshen() throws IOException {
        chatBoxList = usrDao.getAllChat(Storage.user.getUserName());
        friendList = usrDao.getAllFriends(Storage.user.getUserName());
        if (group_bar_chatWindow.isVisible() && chatBoxList.contains(friendName))
            loadChatWindow(friendName);
        else if(group_bar_chatWindow.isVisible() && !chatBoxList.contains(friendName)){
            chatWindowFlag = false;
            group_bar_chatWindow.setVisible(false);
            loadChatBoxList();
        }
        else if (group_bar_chatboxlist.isVisible())
            loadChatBoxList();
        else if (group_bar_friend.isVisible())
            loadFriendBar();
    }
}
