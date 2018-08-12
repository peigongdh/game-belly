package com.peigongdh.gameregister.map;

import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

public class GateConnectionMap {

    private static final Logger logger = LoggerFactory.getLogger(GateConnectionMap.class);

    public static ConcurrentHashMap<Long, GateConnection> gateConnectionMap = new ConcurrentHashMap<>();

    public static GateConnection getGateConnection(ChannelHandlerContext ctx) {
        Long gateId = ctx.channel().attr(GateConnection.GATE_ID).get();
        return getGateConnection(gateId);
    }

    public static GateConnection getGateConnection(Long gateId) {
        GateConnection conn = gateConnectionMap.get(gateId);
        if (conn != null) {
            return conn;
        } else {
            logger.error("gateId {} not found in GateConnectionMap", gateId);
        }
        return null;
    }

    public static void addGateConnection(ChannelHandlerContext ctx) {
        // FIXME: login again should remove one of before
        GateConnection conn = new GateConnection(ctx);

        if (gateConnectionMap.putIfAbsent(conn.getGateId(), conn) != null) {
            logger.error("duplicated gateId");
        }
    }

    public static void removeGateConnection(ChannelHandlerContext ctx) {
        GateConnection conn = getGateConnection(ctx);
        if (null == conn) {
            logger.error("conn not found when remove GateConnection");
            return;
        }
        Long gateId = conn.getGateId();
        if (gateConnectionMap.remove(gateId) != null) {
            // TODO
        } else {
            logger.error("gateId {} not found in GateConnectionMap", gateId);
        }
    }
}
