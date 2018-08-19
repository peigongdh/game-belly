package com.peigongdh.gamegate.handler;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RegisterHandler extends SimpleChannelInboundHandler<String> {

    private static final Logger logger = LoggerFactory.getLogger(RegisterHandler.class);

    private static final String EVENT_BROADCAST_ADDRESS = "broadcast_address";

    private static final String EVENT_GATE_CONNECT = "gate_connect";

    private static final String EVENT_INNER_CONNECT = "inner_connect";

    private String gateLanIp;

    private int gateLanPort;

    public RegisterHandler(String gateLanIp, int gateLanPort) {
        this.gateLanIp = gateLanIp;
        this.gateLanPort = gateLanPort;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("event", EVENT_GATE_CONNECT);
        jsonObject.put("address", gateLanIp + ':' + gateLanPort);
        String json = jsonObject.toJSONString();
        ctx.writeAndFlush(json);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        logger.info("channelRead0: {}", msg);
        JSONObject msgObject = JSONObject.parseObject(msg);
        String event = msgObject.getString("event");
        switch (event) {
            case EVENT_BROADCAST_ADDRESS:
                // TODO
                logger.info("EVENT_BROADCAST_ADDRESS {}");
                break;
            default:
                ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
