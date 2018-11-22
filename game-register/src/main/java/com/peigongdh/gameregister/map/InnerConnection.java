package com.peigongdh.gameregister.map;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

import java.util.concurrent.atomic.AtomicLong;

public class InnerConnection {

    private static final AtomicLong InnerIdGenerator = new AtomicLong(0);

    public static AttributeKey<Long> INNER_ID = AttributeKey.valueOf("innerId");

    private Long innerId;

    private String address;

    private ChannelHandlerContext ctx;

    private static Long generateInnerId() {
        return InnerIdGenerator.incrementAndGet();
    }

    InnerConnection(ChannelHandlerContext ctx) {
        this.innerId = generateInnerId();
        this.ctx = ctx;
        this.ctx.channel().attr(InnerConnection.INNER_ID).setIfAbsent(this.innerId);
    }

    public Long getInnerId() {
        return innerId;
    }

    public void setInnerId(Long innerId) {
        this.innerId = innerId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }
}
