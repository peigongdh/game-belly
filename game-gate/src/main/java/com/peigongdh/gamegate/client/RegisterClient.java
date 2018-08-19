package com.peigongdh.gamegate.client;

import com.peigongdh.gamegate.handler.RegisterChannelInitializer;
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

    private String gateLanIp;

    private int gateLanPort;

    private Bootstrap bootstrap;

    private Channel channel;

    private boolean init() {
        boolean initSuccess = true;
        try {
            Properties properties = new Properties();
            properties.load(RegisterClient.class.getClassLoader().getResourceAsStream(CLIENT_PROPERTIES));
            registerHostName = properties.getProperty("register.hostname");
            registerPort = Integer.parseInt(properties.getProperty("register.port"));
            gateLanIp = properties.getProperty("gate.lanIp");
            gateLanPort = Integer.parseInt(properties.getProperty("gate.lanPort"));
        } catch (IOException e) {
            initSuccess = false;
            e.printStackTrace();
        }
        return initSuccess;
    }

    public void start() {
        boolean initResult = init();
        if (!initResult) {
            logger.info("in game gate register client init error");
        }
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new RegisterChannelInitializer(this, gateLanIp, gateLanPort));

            connect();
        } finally {
            group.shutdownGracefully();
        }
    }

    public void connect() {
        try {
            channel = bootstrap.connect(registerHostName, registerPort).sync().channel();
            logger.info("in game gate register client connect success");
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
