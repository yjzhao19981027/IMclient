package sample.socket;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import sample.util.Storage;

import java.util.concurrent.Callable;


public class NettyClient implements Callable<Channel> {
    private Channel channel;

    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    private static NettyClient instance = new NettyClient();

    private NettyClient(){}

    public static NettyClient getInstance(){
        return instance;
    }

    @Override
    public Channel call() throws Exception{
        ChannelFuture channelFuture = null;
        try {
            Bootstrap b = new Bootstrap();
            b.group(this.workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.AUTO_READ, true);
            b.handler(new MyChannelInitializer());
            channelFuture = b.connect(Storage.INET_HOST, Storage.INET_PORT).syncUninterruptibly();
            this.channel = channelFuture.channel();
            Storage.channel = this.channel;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != channelFuture && channelFuture.isSuccess())
                System.out.println("socket client start done!");
        }
        return this.channel;
    }

    public void destroy() {
        if (null == channel) return;
        channel.close();
        workerGroup.shutdownGracefully();
    }

    public boolean isActive(){
        return channel.isActive();
    }

    public Channel channel() {
        return channel;
    }
}



