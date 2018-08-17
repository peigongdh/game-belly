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

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) {
        inboundChannel.writeAndFlush(new TextWebSocketFrame(s)).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                // was able to flush out data, start to read the next chunk
                logger.info("ProxyBackendHandler {} isSuccess: {}", ctx.channel(), future.isSuccess());
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
