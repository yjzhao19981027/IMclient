package sample.Entity;


import java.util.Date;

public class Msg {
    private String senderName;
    private String receiverName;
    private String msg;
    private String img;
    private Date time;
    private String msgType;
    private boolean isRead;


    public Msg(String senderName, String receiverName, String msg, String img, Date time, String msgType){
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.msg = msg;
        this.img = img;
        this.time = time;
        this.msgType = msgType;
        this.isRead = false;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getMsg() {
        return msg;
    }

    public String getImg() {
        return img;
    }

    public Date getTime() {
        return time;
    }

    public String getMsgType() {
        return msgType;
    }

    public boolean isRead() {
        return isRead;
    }

}
