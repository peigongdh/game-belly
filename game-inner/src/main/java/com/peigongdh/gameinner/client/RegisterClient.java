package com.peigongdh.gameinner.client;

import com.peigongdh.gameinner.handler.RegisterChannelInitializer;
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

    private String registerHostName;

    private int registerPort;

    private Bootstrap bootstrap;

    private boolean init() {
        boolean initSuccess = true;
        try {
            Properties properties = new Properties();
            properties.load(RegisterClient.class.getClassLoader().getResourceAsStream(CLIENT_PROPERTIES));
            registerHostName = properties.getProperty("register.hostname");
            registerPort = Integer.parseInt(properties.getProperty("register.port"));
        } catch (IOException e) {
            initSuccess = false;
            e.printStackTrace();
        }
        return initSuccess;
    }

    public void start() {
        boolean initResult = init();
        if (!initResult) {
            logger.info("in game inner register client init error");
        }
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new RegisterChannelInitializer(this));

            connect();
        } finally {
            group.shutdownGracefully();
        }
    }

    public void connect() {
        try {
            Channel channel = bootstrap.connect(registerHostName, registerPort).sync().channel();
            logger.info("in game inner register client connect success");
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
