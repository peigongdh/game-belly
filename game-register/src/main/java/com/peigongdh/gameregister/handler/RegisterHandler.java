package com.peigongdh.gameregister.handler;

import com.alibaba.fastjson.JSONObject;
import com.peigongdh.gameregister.map.GateConnection;
import com.peigongdh.gameregister.map.GateConnectionMap;
import com.peigongdh.gameregister.map.InnerConnection;
import com.peigongdh.gameregister.map.InnerConnectionMap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

@ChannelHandler.Sharable
public class RegisterHandler extends SimpleChannelInboundHandler<String> {

    private static final Logger logger = LoggerFactory.getLogger(RegisterHandler.class);

    private static final String EVENT_GATE_CONNECT = "gate_connect";

    private static final String EVENT_INNER_CONNECT = "inner_connect";

    private static final String EVENT_BROADCAST_ADDRESS = "broadcast_address";

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
        logger.info("channelRead0: {}", msg);
        JSONObject msgObject = JSONObject.parseObject(msg);
        String event = msgObject.getString("event");
        if (event != null) {
            switch (event) {
                case EVENT_GATE_CONNECT:
                    String address = msgObject.getString("address");
                    GateConnectionMap.addGateConnection(ctx, address);
                    String gateAddress = this.getGateAddress();
                    for (InnerConnection innerConnection : InnerConnectionMap.innerConnectionMap.values()) {
                        innerConnection.getCtx().writeAndFlush(gateAddress);
                    }
                    break;
                case EVENT_INNER_CONNECT:
                    InnerConnectionMap.addInnerConnection(ctx);
                    ctx.writeAndFlush(this.getGateAddress());
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
        // FIXME: bug exist, how to close?
        // cause.printStackTrace();
        // if is gate channel broadcast to each inner channel
        this.closeRegisterClient(ctx);
        cause.printStackTrace();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        this.closeRegisterClient(ctx);
        logger.error("register client disconnected!");
    }

    private String getGateAddress() {
        JSONObject data = new JSONObject();
        Set<String> addresses = new HashSet<>();
        for (GateConnection gateConnection : GateConnectionMap.gateConnectionMap.values()) {
            addresses.add(gateConnection.getAddress());
        }
        data.put("event", EVENT_BROADCAST_ADDRESS);
        data.put("addresses", addresses);
        return data.toJSONString();
    }

    private void closeRegisterClient(ChannelHandlerContext ctx) {
        if (ctx == null) {
            return;
        }
        // FIXME: ctx.channel().hasAttr(GateConnection.GATE_ID) = TRUE & ctx.channel().hasAttr(InnerConnection.INNER_ID) = TRUE, why ?
        if (ctx.channel().attr(GateConnection.GATE_ID).get() != null) {
            GateConnectionMap.removeGateConnection(ctx);
        }
        if (ctx.channel().attr(InnerConnection.INNER_ID).get() != null) {
            String gateAddress = this.getGateAddress();
            for (InnerConnection innerConnection : InnerConnectionMap.innerConnectionMap.values()) {
                innerConnection.getCtx().writeAndFlush(gateAddress);
            }
            InnerConnectionMap.removeInnerConnection(ctx);
        }
        ctx.close();
    }
}
