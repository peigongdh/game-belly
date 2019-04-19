package com.peigongdh.gamegate.handler;

import com.peigongdh.apidoc.register.RegisterProto;
import com.peigongdh.apidoc.register.RegisterProto.Connect;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RegisterHandler extends SimpleChannelInboundHandler<byte[]> {

    private static final Logger logger = LoggerFactory.getLogger(RegisterHandler.class);

    private String gateLanIp;

    private int gateLanPort;

    RegisterHandler(String gateLanIp, int gateLanPort) {
        this.gateLanIp = gateLanIp;
        this.gateLanPort = gateLanPort;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        Connect.Builder connect = Connect.newBuilder();
        connect.setEvent(RegisterProto.EventType.GATE_CONNECT);
        connect.setAddress(gateLanIp + ':' + gateLanPort);
        byte[] msg = connect.build().toByteArray();
        ctx.writeAndFlush(msg);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) {
        logger.info("game gate register client channelRead0: {}", msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
