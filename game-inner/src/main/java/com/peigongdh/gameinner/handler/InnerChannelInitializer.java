package com.peigongdh.gameinner.handler;

import com.peigongdh.gameinner.browserquest.domain.World;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

public class InnerChannelInitializer extends ChannelInitializer<SocketChannel> {

    // FIXME: now test for workerman frame protocol
    private static final LengthFieldPrepender LENGTH_FIELD_PREPENDER = new LengthFieldPrepender(Integer.BYTES, true);

    private static final StringDecoder STRING_DECODER = new StringDecoder(CharsetUtil.UTF_8);

    private static final StringEncoder STRING_ENCODER = new StringEncoder(CharsetUtil.UTF_8);

    private World world;

    public InnerChannelInitializer(World world) {
        this.world = world;
    }

    protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        // LengthFieldBasedFrameDecoder extends ByteToMessageDecoder
        // Be aware that sub-classes of ByteToMessageDecoder MUST NOT annotated with @Sharable.
        pipeline.addLast(new LoggingHandler(LogLevel.INFO));
        pipeline.addLast(new IdleStateHandler(0, 0, 5));
        // pipeline.addLast(new HeartbeatHandler());
        // FIXME: now test for workerman frame protocol
        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, Integer.BYTES, -Integer.BYTES, Integer.BYTES));
        pipeline.addLast(LENGTH_FIELD_PREPENDER);
        // the encoder and decoder are static as these are sharable
        pipeline.addLast(STRING_DECODER);
        pipeline.addLast(STRING_ENCODER);
        pipeline.addLast(new InnerHandler(world));
    }
}
