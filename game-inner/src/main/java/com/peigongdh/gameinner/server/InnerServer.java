package com.peigongdh.gameinner.server;

import com.peigongdh.gameinner.browserquest.domain.World;
import com.peigongdh.gameinner.handler.InnerChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

public class InnerServer {

    private static final Logger logger = LoggerFactory.getLogger(InnerServer.class);

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
            properties.load(InnerServer.class.getClassLoader().getResourceAsStream(SERVER_PROPERTIES));
            port = Integer.parseInt(properties.getProperty("inner.port"));
            masterCount = Integer.parseInt(properties.getProperty("inner.masterCount"));
            workerCount = Integer.parseInt(properties.getProperty("inner.workerCount"));
            keepAlive = Boolean.parseBoolean(properties.getProperty("inner.keepAlive"));
            backlog = Integer.parseInt(properties.getProperty("inner.backlog"));
        } catch (IOException e) {
            initSuccess = false;
            e.printStackTrace();
        }
        return initSuccess;
    }

    public void start(World world) {
        boolean initResult = init();
        if (!initResult) {
            logger.info("inner server init error");
        }
        EventLoopGroup masterGroup = new NioEventLoopGroup(masterCount);
        EventLoopGroup workerGroup = new NioEventLoopGroup(workerCount);
        try {
            serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(masterGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new InnerChannelInitializer(world))
                    .option(ChannelOption.SO_BACKLOG, backlog)
                    .childOption(ChannelOption.SO_KEEPALIVE, keepAlive)
                    .childOption(ChannelOption.TCP_NODELAY, true);
            channel = serverBootstrap.bind(port).sync().channel();
            logger.info("inner server start success");
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            logger.info("inner server start error");
            e.printStackTrace();
        } finally {
            masterGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}