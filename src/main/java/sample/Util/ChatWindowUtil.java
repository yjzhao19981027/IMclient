package sample.Util;

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
}
