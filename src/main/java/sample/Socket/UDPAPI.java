package sample.Socket;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPAPI {
    DatagramSocket sockid;
    DatagramPacket pac;

    public UDPAPI() throws SocketException {
        sockid = new DatagramSocket();
    }

    public DatagramPacket getPac() {
        return pac;
    }

    public DatagramSocket getSockid() {
        return sockid;
    }

    public void sendData(String data) throws Exception{
        byte[] buf = data.getBytes();
        pac = new DatagramPacket(buf,buf.length, InetAddress.getByName("127.0.0.1"),8000);
        sockid.send(pac);
        pac = null;
    }

    public void recvData() throws Exception{
        byte[] rec = new byte[1024];
        pac = new DatagramPacket(rec,rec.length);
        sockid.receive(pac);
    }
}
