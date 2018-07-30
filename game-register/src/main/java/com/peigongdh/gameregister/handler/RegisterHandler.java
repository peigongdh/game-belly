package com.peigongdh.gameregister.handler;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

@ChannelHandler.Sharable
public class RegisterHandler extends SimpleChannelInboundHandler<String> {

    private static final Logger logger = LoggerFactory.getLogger(RegisterHandler.class);

    /**
     * A thread-safe Set  Using ChannelGroup, you can categorize Channels into a meaningful group.
     * A closed Channel is automatically removed from the collection,
     */
    private static final ChannelGroup gateChannels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private static final ChannelGroup innerChannels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private static final String EVENT_GATE_CONNECT = "gate_connect";

    private static final String EVENT_INNER_CONNECT = "inner_connect";

    private static final String EVENT_PING = "ping";

    private static final String EVENT_PONG = "pong";

    private static final String EVENT_BROADCAST_ADDRESS = "broadcast_address";

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        logger.info("channelRead0: {}", msg);
        JSONObject msgObject = JSONObject.parseObject(msg);
        String event = msgObject.getString("event");
        if (event != null) {
            switch (event) {
                case EVENT_GATE_CONNECT:
                    gateChannels.add(ctx.channel());
                    String gateAddress = this.getGateAddress();
                    for (Channel c : innerChannels) {
                        c.writeAndFlush(gateAddress);
                    }
                    break;
                case EVENT_INNER_CONNECT:
                    innerChannels.add(ctx.channel());
                    ctx.writeAndFlush(this.getGateAddress());
                    break;
                case EVENT_PING:
                    // TODO: heartbeat
                    break;
                default:
                    logger.error("register unknown event: {}", msg);
                    ctx.close();
            }
        } else {
            logger.error("register unknown event: {}", msg);
            ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        // if is gate channel broadcast to each inner channel
        if (gateChannels.contains(ctx.channel())) {
            String gateAddress = this.getGateAddress();
            for (Channel c : innerChannels) {
                c.writeAndFlush(gateAddress);
            }
        }
        ctx.close();
    }

    private String getGateAddress() {
        JSONObject data = new JSONObject();
        Set<String> address = new HashSet<>();
        for (Channel c : gateChannels) {
            address.add(c.remoteAddress().toString());
        }
        data.put("event", EVENT_BROADCAST_ADDRESS);
        data.put("address", address);
        return data.toJSONString();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        logger.error("register client disconnected!");
    }
}