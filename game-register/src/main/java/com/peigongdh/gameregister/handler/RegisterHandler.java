package com.peigongdh.gameregister.handler;

import com.google.protobuf.InvalidProtocolBufferException;
import com.peigongdh.apidoc.register.RegisterProto;
import com.peigongdh.gameregister.map.GateConnection;
import com.peigongdh.gameregister.map.GateConnectionMap;
import com.peigongdh.gameregister.map.InnerConnection;
import com.peigongdh.gameregister.map.InnerConnectionMap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegisterHandler extends SimpleChannelInboundHandler<byte[]> {

    private static final Logger logger = LoggerFactory.getLogger(RegisterHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) {
        try {
            RegisterProto.Connect msgObject = RegisterProto.Connect.parseFrom(msg);
            logger.info("channelRead0: {}", msgObject.toString());
            switch (msgObject.getEvent().getNumber()) {
                case RegisterProto.EventType.GATE_CONNECT_VALUE:
                    String address = msgObject.getAddress();
                    GateConnectionMap.addGateConnection(ctx, address);
                    String gateAddress = new String(this.getGateAddress());
                    for (InnerConnection innerConnection : InnerConnectionMap.innerConnectionMap.values()) {
                        innerConnection.getCtx().writeAndFlush(gateAddress);
                    }
                    break;
                case RegisterProto.EventType.INNER_CONNECT_VALUE:
                    InnerConnectionMap.addInnerConnection(ctx);
                    ctx.writeAndFlush(this.getGateAddress());
                    break;
                default:
                    logger.error("register unknown event: {}", msg);
                    ctx.close();
            }
        } catch (InvalidProtocolBufferException e) {
            logger.error("unknown msg: {}", msg);
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

    private byte[] getGateAddress() {
        RegisterProto.BroadcastAddress.Builder broadcastAddress = RegisterProto.BroadcastAddress.newBuilder();
        for (GateConnection gateConnection : GateConnectionMap.gateConnectionMap.values()) {
            broadcastAddress.addAddresses(gateConnection.getAddress());
        }
        broadcastAddress.setEvent(RegisterProto.EventType.BROADCAST_ADDRESS);
        return broadcastAddress.build().toByteArray();
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
            String gateAddress = new String(this.getGateAddress());
            for (InnerConnection innerConnection : InnerConnectionMap.innerConnectionMap.values()) {
                innerConnection.getCtx().writeAndFlush(gateAddress);
            }
            InnerConnectionMap.removeInnerConnection(ctx);
        }
        ctx.close();
    }
}
