package sample.Entity;


import java.util.Date;

public class Msg {
    private String senderName;
    private String receiverName;
    private String msg;
    private Date time;
    private String msgType;
    private boolean isRead;

    public Msg(String senderName,String receiverName,String msg,Date time,String msgType){
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.msg = msg;
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
