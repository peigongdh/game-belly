package com.peigongdh.gamegate.handler;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.peigongdh.gamegate.util.HandlerUtil;

public class WebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketFrameHandler.class);

    private String innerHostName;

    private int innerPort;

    private Channel outboundChannel;

    public WebSocketFrameHandler(String innerHostName, int innerPort) {
        this.innerHostName = innerHostName;
        this.innerPort = innerPort;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        final Channel inboundChannel = ctx.channel();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(inboundChannel.eventLoop())
                .channel(ctx.channel().getClass())
                .handler(new ProxyBackendHandler(inboundChannel))
                .option(ChannelOption.AUTO_READ, false);
        ChannelFuture channelFuture = bootstrap.connect(innerHostName, innerPort);
        outboundChannel = channelFuture.channel();
        channelFuture.addListener((ChannelFutureListener) channelFuture1 -> {
            if (channelFuture1.isSuccess()) {
                inboundChannel.read();
            } else {
                inboundChannel.close();
            }
        });
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        // ping and pong frames already handled

        if (frame instanceof TextWebSocketFrame) {
            if (outboundChannel.isActive()) {
                // FIXME: PooledUnsafeDirectByteBuf
                ByteBuf request = frame.copy().content();
                logger.info("{} received {}", ctx.channel(), request.toString());
                outboundChannel.writeAndFlush(request).addListener((ChannelFutureListener) future -> {
                    if (future.isSuccess()) {
                        // was able to flush out data, start to read the next chunk
                        logger.info("WebSocketFrameHandler {} isSuccess: {}", ctx.channel(), future.isSuccess());
                        ctx.channel().read();
                    } else {
                        logger.info("WebSocketFrameHandler {} cause: {}", ctx.channel(), future.cause());
                        future.channel().close();
                    }
                });
            }
        } else {
            String message = "unsupported frame type: " + frame.getClass().getName();
            throw new UnsupportedOperationException(message);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        if (outboundChannel != null) {
            HandlerUtil.closeOnFlush(outboundChannel);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        HandlerUtil.closeOnFlush(ctx.channel());
    }
}
