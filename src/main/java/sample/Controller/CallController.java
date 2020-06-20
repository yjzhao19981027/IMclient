package sample.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class CallController implements Initializable {
    @FXML
    private Pane CallPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void disconnectAction(ActionEvent actionEvent) {
        Stage stage = (Stage) CallPane.getScene().getWindow();
        stage.close();
    }
}
