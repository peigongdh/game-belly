package com.peigongdh.gamegate.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;

public class ProxyBackendInitializer extends ChannelInitializer<SocketChannel> {

    private static final LengthFieldPrepender LENGTH_FIELD_PREPENDER = new LengthFieldPrepender(Integer.BYTES);

    private static final StringDecoder STRING_DECODER = new StringDecoder(CharsetUtil.UTF_8);

    private static final StringEncoder STRING_ENCODER = new StringEncoder(CharsetUtil.UTF_8);

    private final Channel inboundChannel;

    ProxyBackendInitializer(Channel inboundChannel) {
        this.inboundChannel = inboundChannel;
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new LoggingHandler(LogLevel.INFO));
        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, Integer.BYTES, 0, Integer.BYTES));
        pipeline.addLast(LENGTH_FIELD_PREPENDER);
        pipeline.addLast(STRING_DECODER);
        pipeline.addLast(STRING_ENCODER);
        pipeline.addLast(new ProxyBackendHandler(inboundChannel));
    }
}
