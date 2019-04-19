package com.peigongdh.gameinner.handler;

import com.google.protobuf.InvalidProtocolBufferException;
import com.peigongdh.apidoc.register.RegisterProto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegisterHandler extends SimpleChannelInboundHandler<byte[]> {

    private static final Logger logger = LoggerFactory.getLogger(RegisterHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        RegisterProto.Connect.Builder connect = RegisterProto.Connect.newBuilder();
        connect.setEvent(RegisterProto.EventType.INNER_CONNECT);
        byte[] msg = connect.build().toByteArray();
        ctx.writeAndFlush(msg);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) {
        try {
            RegisterProto.BroadcastAddress msgObject = RegisterProto.BroadcastAddress.parseFrom(msg);
            logger.info("channelRead0: {}", msgObject.toString());
            switch (msgObject.getEvent().getNumber()) {
                case RegisterProto.EventType.BROADCAST_ADDRESS_VALUE:
                    for (String address : msgObject.getAddressesList()) {
                        logger.info("address {}: ", address);
                    }
                    break;
                default:
                    logger.info("undefined event {}", msg);
                    ctx.close();
            }
        } catch (InvalidProtocolBufferException e) {
            logger.error("unknown msg: {}", msg);
            ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
