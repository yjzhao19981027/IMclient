package sample.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sample.Util.Storage;

import java.net.URL;
import java.util.ResourceBundle;

public class CallController implements Initializable {
    @FXML
    public Pane CallPane;
    @FXML
    private Label connecting;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Storage.callController = CallController.this;
    }

    public void disconnectAction(ActionEvent actionEvent) {
        Storage.channel.writeAndFlush("cancel " + Storage.callFriend + "\r\n");
        Stage stage = (Stage) CallPane.getScene().getWindow();
        stage.close();
    }
}
