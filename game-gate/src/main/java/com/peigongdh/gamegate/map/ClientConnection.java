package com.peigongdh.gamegate.map;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

import java.util.concurrent.atomic.AtomicLong;

public class ClientConnection {

    // FIXME: consider distributed env
    private static final AtomicLong clientIdGenerator = new AtomicLong(0);

    public static AttributeKey<Long> CLIENT_ID = AttributeKey.valueOf("clientId");

    private Long clientId;

    private String userId;

    private ChannelHandlerContext ctx;

    private static Long generateClientId() {
        return clientIdGenerator.incrementAndGet();
    }

    public ClientConnection(ChannelHandlerContext ctx) {
        this.clientId = generateClientId();
        this.ctx = ctx;
        this.ctx.channel().attr(ClientConnection.CLIENT_ID).set(clientId);
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }
}
