package com.peigongdh.gamegate.client;

import com.peigongdh.gamegate.handler.RegisterChannelInitializer;
import com.peigongdh.gamegate.server.WebSocketServer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

public class RegisterClient {

    private static final Logger logger = LoggerFactory.getLogger(RegisterClient.class);

    private final static String CLIENT_PROPERTIES = "application.properties";

    private String hostName;

    private int port;

    private Bootstrap bootstrap;

    private Channel channel;

    private boolean init() {
        boolean initSuccess = true;
        try {
            Properties properties = new Properties();
            properties.load(WebSocketServer.class.getClassLoader().getResourceAsStream(CLIENT_PROPERTIES));
            hostName = properties.getProperty("register.hostname");
            port = Integer.parseInt(properties.getProperty("register.port"));
        } catch (IOException e) {
            initSuccess = false;
            e.printStackTrace();
        }
        return initSuccess;
    }

    public void start() {
        boolean initResult = init();
        if (!initResult) {
            logger.info("register client init error");
        }
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new RegisterChannelInitializer(this, hostName, port));

            connect();
        } finally {
            group.shutdownGracefully();
        }
    }

    public void connect() {
        try {
            channel = bootstrap.connect(hostName, port).sync().channel();
            logger.info("register client connect success");
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
