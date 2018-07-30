package com.peigongdh.gamegate.server;

import com.peigongdh.gamegate.handler.WebSocketChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

public class WebSocketServer {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

    private final static String SERVER_PROPERTIES = "application.properties";

    private int masterCount;

    private int workerCount;

    private boolean keepAlive;

    private int backlog;

    private String innerHostname;

    private int innerPort;

    private int webSocketPort;

    private ServerBootstrap serverBootstrap;

    private Channel channel;

    public ServerBootstrap getServerBootstrap() {
        return serverBootstrap;
    }

    public Channel getChannel() {
        return channel;
    }

    private boolean init() {
        boolean initSuccess = true;
        try {
            Properties properties = new Properties();
            properties.load(WebSocketServer.class.getClassLoader().getResourceAsStream(SERVER_PROPERTIES));
            masterCount = Integer.parseInt(properties.getProperty("websocket.masterCount"));
            workerCount = Integer.parseInt(properties.getProperty("websocket.workerCount"));
            keepAlive = Boolean.parseBoolean(properties.getProperty("websocket.keepAlive"));
            backlog = Integer.parseInt(properties.getProperty("websocket.backlog"));
            webSocketPort = Integer.parseInt(properties.getProperty("websocket.port"));
            innerHostname = properties.getProperty("inner.hostname");
            innerPort = Integer.parseInt(properties.getProperty("inner.port"));
        } catch (IOException e) {
            initSuccess = false;
            e.printStackTrace();
        }
        return initSuccess;
    }

    public void start() {
        boolean initResult = init();
        if (!initResult) {
            logger.info("websocket server init error");
        }
        EventLoopGroup masterGroup = new NioEventLoopGroup(masterCount);
        EventLoopGroup workerGroup = new NioEventLoopGroup(workerCount);
        try {
            serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(masterGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new WebSocketChannelInitializer(innerHostname, innerPort))
                    .option(ChannelOption.SO_KEEPALIVE, keepAlive)
                    .option(ChannelOption.SO_BACKLOG, backlog);
            channel = serverBootstrap.bind(webSocketPort).sync().channel();
            logger.info("websocket server start success");
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            logger.info("websocket server start error");
            e.printStackTrace();
        } finally {
            masterGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
