package com.peigongdh.gamegate.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class RegisterHandlerInitializer extends ChannelInitializer<SocketChannel> {

    private static final StringDecoder STRING_DECODER = new StringDecoder();

    private static final StringEncoder STRING_ENCODER = new StringEncoder();

    private String hostName;

    private int port;

    public RegisterHandlerInitializer(String hostName, int port) {
        this.hostName = hostName;
        this.port = port;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        pipeline.addLast(STRING_DECODER);
        pipeline.addLast(STRING_ENCODER);
        pipeline.addLast(new RegisterHandler(hostName, port));
    }
}
