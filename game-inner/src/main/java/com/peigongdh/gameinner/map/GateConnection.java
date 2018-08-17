package com.peigongdh.gameinner.map;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

import java.util.concurrent.atomic.AtomicLong;

public class GateConnection {
    
    private static final AtomicLong gateIdGenerator = new AtomicLong(0);

    public static AttributeKey<Long> GATE_ID = AttributeKey.valueOf("gateId");

    private Long gateId;

    private String address;

    private ChannelHandlerContext ctx;

    private static Long generateGateId() {
        return gateIdGenerator.incrementAndGet();
    }

    public GateConnection(ChannelHandlerContext ctx) {
        this.gateId = generateGateId();
        this.ctx = ctx;
    }

    public Long getGateId() {
        return gateId;
    }

    public void setGateId(Long gateId) {
        this.gateId = gateId;
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
