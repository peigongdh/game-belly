package com.peigongdh.gamegate.handler;

import io.netty.channel.*;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.peigongdh.gamegate.util.HandlerUtil;

public class ProxyBackendHandler extends SimpleChannelInboundHandler<String> {

    private static final Logger logger = LoggerFactory.getLogger(ProxyBackendHandler.class);

    private final Channel inboundChannel;

    ProxyBackendHandler(Channel inboundChannel) {
        this.inboundChannel = inboundChannel;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.read();
    }


    // FIXME: BUG here, when game-client reload
//    2018-11-19 19:35:51 INFO  ProxyBackendHandler:32 - ProxyBackendHandler [id: 0x4c089f87, L:/127.0.0.1:57113 - R:localhost/127.0.0.1:8001] cause: {}
//    java.lang.UnsupportedOperationException: unsupported message type: TextWebSocketFrame (expected: ByteBuf, FileRegion)
//        at io.netty.channel.nio.AbstractNioByteChannel.filterOutboundMessage(AbstractNioByteChannel.java:283)
//        at io.netty.channel.AbstractChannel$AbstractUnsafe.write(AbstractChannel.java:877)
//        at io.netty.channel.DefaultChannelPipeline$HeadContext.write(DefaultChannelPipeline.java:1391)
//        at io.netty.channel.AbstractChannelHandlerContext.invokeWrite0(AbstractChannelHandlerContext.java:738)

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) {
        inboundChannel.writeAndFlush(new TextWebSocketFrame((s))).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                // was able to flush out data, start to read the next chunk
                ctx.channel().read();
            } else {
                logger.info("ProxyBackendHandler {} cause: {}", ctx.channel(), future.cause());
                future.channel().close();
            }
        });
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        HandlerUtil.closeOnFlush(inboundChannel);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        HandlerUtil.closeOnFlush(ctx.channel());
    }
}
