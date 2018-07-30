package com.peigongdh.gamegate.handler;

import com.peigongdh.gamegate.client.RegisterClient;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegisterHeartbeatHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(RegisterHeartbeatHandler.class);

    private static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled.unreleasableBuffer(
            Unpooled.copiedBuffer("HEARTBEAT", CharsetUtil.UTF_8));

    private RegisterClient registerClient;

    public RegisterHeartbeatHandler(RegisterClient registerClient) {
        this.registerClient = registerClient;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            ctx.writeAndFlush(HEARTBEAT_SEQUENCE.duplicate())
                    .addListener((ChannelFutureListener) future -> {
                        if (!future.isSuccess()) {
                            // try to reconnect
                            logger.info("try to reconnect");
                            registerClient.connect();
                        } else {
                            logger.info("client heartbeat success");
                        }
                    });
            // .addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
