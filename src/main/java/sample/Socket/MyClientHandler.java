package sample.Socket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import javafx.application.Platform;
import sample.Util.Storage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MyClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        SocketChannel channel = (SocketChannel) ctx.channel();
        System.out.println("链接报告开始");
        System.out.println("链接报告信息：本客户端链接到服务端。channelId：" + channel.id());
        System.out.println("链接报告 IP:" + channel.localAddress().getHostString());
        System.out.println("链接报告 Port:" + channel.localAddress().getPort());
        System.out.println("链接报告完毕");
//         通知客户端链接建立成功
//        String str = "通知服务端链接建立成功" + "" + new Date()+" "+ channel.localAddress().getHostString() +"\r\n";
//        ctx.writeAndFlush(str);
    }
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("断开链接" + ctx.channel().localAddress().toString());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 接收 msg 消息
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "接收到消息：" + msg);
        if (msg.equals("refreshen")){
            Platform.runLater(new Runnable() {
                @Override
                public void run(){
                    try {
                        Storage.chatController.refreshen();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * 抓住异常，当发生异常的时候，可以做一些相应的处理，比如打印日志、关闭链接
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {ctx.close();
        System.out.println("异常信息：\r\n" + cause.getMessage());
    }
}
