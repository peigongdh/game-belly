package com.peigongdh.gameregister.server;

import com.peigongdh.gameregister.handler.RegisterChannelInitializer;
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

public class RegisterServer {

    private static final Logger logger = LoggerFactory.getLogger(RegisterServer.class);

    private final static String SERVER_PROPERTIES = "application.properties";

    private int masterCount;

    private int workerCount;

    private boolean keepAlive;

    private int backlog;

    private int port;

    private ServerBootstrap serverBootstrap;

    private Channel channel;

    private boolean init() {
        boolean initSuccess = true;
        try {
            Properties properties = new Properties();
            properties.load(RegisterServer.class.getClassLoader().getResourceAsStream(SERVER_PROPERTIES));
            port = Integer.parseInt(properties.getProperty("register.port"));
            masterCount = Integer.parseInt(properties.getProperty("register.masterCount"));
            workerCount = Integer.parseInt(properties.getProperty("register.workerCount"));
            keepAlive = Boolean.parseBoolean(properties.getProperty("register.keepAlive"));
            backlog = Integer.parseInt(properties.getProperty("register.backlog"));
        } catch (IOException e) {
            initSuccess = false;
            e.printStackTrace();
        }
        return initSuccess;
    }

    public void start() {
        boolean initResult = init();
        if (!initResult) {
            logger.info("register server init error");
        }
        EventLoopGroup masterGroup = new NioEventLoopGroup(masterCount);
        EventLoopGroup workerGroup = new NioEventLoopGroup(workerCount);
        try {
            serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(masterGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new RegisterChannelInitializer())
                    .option(ChannelOption.SO_KEEPALIVE, keepAlive)
                    .option(ChannelOption.SO_BACKLOG, backlog);
            channel = serverBootstrap.bind(port).sync().channel();
            logger.info("register server start success");
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            logger.info("register server start error");
            e.printStackTrace();
        } finally {
            masterGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
