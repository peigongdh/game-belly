package com.peigongdh.gamegate.client;

import com.peigongdh.gamegate.handler.RegisterHandlerInitializer;
import com.peigongdh.gamegate.server.WebSocketServer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegisterClient {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

    private Bootstrap bootstrap;

    private boolean init() {
        return true;
    }

    public void start() {
        boolean initResult = init();
        if (!initResult) {
            logger.info("register client init error");
        }
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            bootstrap = new Bootstrap();
        } finally {
            group.shutdownGracefully();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new RegisterHandlerInitializer());
        }
    }

}
