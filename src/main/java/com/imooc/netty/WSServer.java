package com.imooc.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class WSServer {

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ServerBootstrap server;
    private ChannelFuture channelFuture;


    private static class StaticWSServer{
        static final WSServer instance = new WSServer();
    }

    public static WSServer getInstance(){
        return StaticWSServer.instance;
    }

    public WSServer() {
         bossGroup = new NioEventLoopGroup();
         workerGroup = new NioEventLoopGroup();
         server = new ServerBootstrap();
         server.group(bossGroup,workerGroup)
               .channel(NioServerSocketChannel.class)
               .childHandler(new WSServerInitializer());
    }

    public void  start(){
        channelFuture = server.bind(8888);
        System.out.println("==============Netty-启动成功===============");
    }
}
