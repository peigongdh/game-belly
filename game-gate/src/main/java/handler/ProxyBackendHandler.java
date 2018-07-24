package handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HandlerUtil;

public class ProxyBackendHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketFrameHandler.class);

    private final Channel inboundChannel;

    ProxyBackendHandler(Channel inboundChannel) {
        this.inboundChannel = inboundChannel;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.read();
    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) {
        inboundChannel.writeAndFlush(new TextWebSocketFrame((ByteBuf) msg)).addListener((ChannelFutureListener) future -> {
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
