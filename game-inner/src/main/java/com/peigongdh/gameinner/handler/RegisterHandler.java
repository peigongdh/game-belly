package com.peigongdh.gameinner.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.peigongdh.gameinner.map.GateConnectionMap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class RegisterHandler extends SimpleChannelInboundHandler<String> {

    private static final Logger logger = LoggerFactory.getLogger(RegisterHandler.class);

    private static final String EVENT_BROADCAST_ADDRESS = "broadcast_address";

    private static final String EVENT_INNER_CONNECT = "inner_connect";

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("event", EVENT_INNER_CONNECT);
        String json = jsonObject.toJSONString();
        logger.info("channelActive {}", json);
        ctx.writeAndFlush(json);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
        logger.info("channelRead0: {}", msg);
        JSONObject msgObject = JSONObject.parseObject(msg);
        String event = msgObject.getString("event");
        switch (event) {
            case EVENT_BROADCAST_ADDRESS:
                JSONArray addresses = msgObject.getJSONArray("addresses");
                for (Object addressObject : addresses) {
                    String address = (String) addressObject;
                    logger.info("address {}: ", address);
                }
                break;
            default:
                logger.info("undefined event {}", event);
                ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
