package com.peigongdh.gameweb.server;

import com.peigongdh.gameweb.handler.HttpStaticFileServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

public class HttpStaticFileServer {

    private static final Logger logger = LoggerFactory.getLogger(HttpStaticFileServer.class);

    private final static String SERVER_PROPERTIES = "application.properties";

    private int masterCount;

    private int workerCount;

    private int port;

    private boolean init() {
        boolean initSuccess = true;
        try {
            Properties properties = new Properties();
            properties.load(HttpStaticFileServer.class.getClassLoader().getResourceAsStream(SERVER_PROPERTIES));
            port = Integer.parseInt(properties.getProperty("web.port"));
            masterCount = Integer.parseInt(properties.getProperty("web.masterCount"));
            workerCount = Integer.parseInt(properties.getProperty("web.workerCount"));
        } catch (IOException e) {
            initSuccess = false;
            e.printStackTrace();
        }
        return initSuccess;
    }

    public void start() {
        boolean initResult = init();
        if (!initResult) {
            logger.info("web server init error");
        }
        EventLoopGroup masterGroup = new NioEventLoopGroup(masterCount);
        EventLoopGroup workerGroup = new NioEventLoopGroup(workerCount);
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(masterGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new HttpStaticFileServerInitializer());
            Channel channel = serverBootstrap.bind(port).sync().channel();
            logger.info("web server start success");
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            logger.info("web server start error");
            e.printStackTrace();
        } finally {
            masterGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
