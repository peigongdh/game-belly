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
    public void channelActive(ChannelHandlerContext ctx) {
        GateConnectionMap.addGateConnection(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
        logger.info("channelRead0: {}", msg);
        JSONObject msgObject = JSONObject.parseObject(msg);
        String event = msgObject.getString("event");
        if (event != null) {
            switch (event) {
                case EVENT_GATE_CONNECT:
                    GateConnectionMap.addGateConnection(ctx);
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
        cause.printStackTrace();
        // if is gate channel broadcast to each inner channel
        GateConnectionMap.removeGateConnection(ctx);
        String gateAddress = this.getGateAddress();
        for (InnerConnection innerConnection : InnerConnectionMap.innerConnectionMap.values()) {
            innerConnection.getCtx().writeAndFlush(gateAddress);
        }
        InnerConnectionMap.removeInnerConnection(ctx);
        ctx.close();
    }

    private String getGateAddress() {
        JSONObject data = new JSONObject();
        Set<String> address = new HashSet<>();
        for (GateConnection gateConnection : GateConnectionMap.gateConnectionMap.values()) {
            address.add(gateConnection.getAddress());
        }
        data.put("event", EVENT_BROADCAST_ADDRESS);
        data.put("address", address);
        return data.toJSONString();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        GateConnectionMap.removeGateConnection(ctx);
        logger.error("register client disconnected!");
    }
}
