package sample.Util;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class ImgUtil {
    //  图片转化为Base64字符串
    public static String imageToBase64(File file){
        BASE64Encoder encoder = new BASE64Encoder();
        byte[] imageBytes = null;
        try  {
            FileInputStream fileInputStream = new FileInputStream(file);
            imageBytes = new byte[fileInputStream.available()];
            fileInputStream.read(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return encoder.encodeBuffer(imageBytes).trim();
    }
    //  Base64字符串转化为图片
    public static Image base64toImage(String str) throws IOException {
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] bytes = decoder.decodeBuffer(str);
        ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
        BufferedImage bi1 =ImageIO.read(stream);
        return SwingFXUtils.toFXImage(bi1,null);
    }

    public static File selectImage(Scene scene){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择图片");
        fileChooser.setInitialDirectory(new File("./"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("IMG","*.jpg","*.png","*.jpeg"));
        File file = fileChooser.showOpenDialog(scene.getWindow());
        return file;
    }
}
