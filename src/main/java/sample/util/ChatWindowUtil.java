package sample.util;

import javafx.scene.Parent;
import javafx.stage.Stage;

public class ChatWindowUtil {
    public double[] setPoints(boolean isMine){
        if (!isMine) {
            return new double[]{
                    0.0, 5.0,
                    10.0, 0.0,
                    10.0, 10.0
            };
        }
        else {
            return new double[]{
                    0.0, 0.0,
                    0.0, 10.0,
                    10.0, 5.0
            };
        }
    }

    public static void setDragged(Parent root,Stage stage){
        root.setOnMousePressed(event -> {
            Storage.xOffset = event.getX();
            Storage.yOffset = event.getY();
        });

        Stage finalStage = stage;
        root.setOnMouseDragged(event -> {
            finalStage.setX(event.getScreenX() - Storage.xOffset);
            finalStage.setY(event.getScreenY() - Storage.yOffset);
        });
    }
}
