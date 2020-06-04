package sample.Util;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import sample.Dao.UserDao;
import sample.Dao.UserDaoImpl;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class ImgUtil {
    //  图片转化为Base64字符串
    public  String imageToBase64(File file){
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
    public Image base64toImage(String str) throws IOException {
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] bytes = decoder.decodeBuffer(str);
        ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
        BufferedImage bi1 =ImageIO.read(stream);
        return SwingFXUtils.toFXImage(bi1,null);
    }
    public static void main(String[] args) throws IOException {
        //图片转化为二进制
        byte[] imageBytes = null;
        try  {
            FileInputStream fileInputStream = new FileInputStream(new File("/Users/zhaoyinjie/Desktop/head1.jpg"));
            imageBytes = new byte[fileInputStream.available()];
            fileInputStream.read(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BASE64Encoder encoder = new BASE64Encoder();
        BASE64Decoder decoder = new BASE64Decoder();
        String str = encoder.encodeBuffer(imageBytes).trim();
        UserDao dao = new UserDaoImpl();
        dao.test(str);
//        String str1 = dao.test1();
//        System.out.println(str1);
//        byte[] bytes = decoder.decodeBuffer(str1);
//        //二进制转化为图片
//        try  {
//            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
//            BufferedImage bi1 =ImageIO.read(bais);
//            Image image = SwingFXUtils.toFXImage(bi1,null);
//            System.out.println(image);
////            FileOutputStream fileOutputStream = new FileOutputStream(new File("/Users/zhaoyinjie/Desktop/head1.jpg"));
////            fileOutputStream.write(bytes);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }


}
