package com.peigongdh.gamegate.handler;

import com.peigongdh.gamegate.client.RegisterClient;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

public class RegisterChannelInitializer extends ChannelInitializer<SocketChannel> {

    private static final LengthFieldPrepender LENGTH_FIELD_PREPENDER = new LengthFieldPrepender(Integer.BYTES);

    private RegisterClient registerClient;

    private String gateLanIp;

    private int gateLanPort;

    public RegisterChannelInitializer(RegisterClient registerClient, String gateLanIp, int gateLanPort) {
        this.registerClient = registerClient;
        this.gateLanIp = gateLanIp;
        this.gateLanPort = gateLanPort;
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new LoggingHandler(LogLevel.INFO));
        pipeline.addLast(new IdleStateHandler(0, 0, 5));
        pipeline.addLast(new RegisterHeartbeatHandler(registerClient));
        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, Integer.BYTES, 0, Integer.BYTES));
        pipeline.addLast(LENGTH_FIELD_PREPENDER);
        pipeline.addLast(new ByteArrayEncoder());
        pipeline.addLast(new ByteArrayDecoder());
        pipeline.addLast(new RegisterHandler(gateLanIp, gateLanPort));
    }
}
