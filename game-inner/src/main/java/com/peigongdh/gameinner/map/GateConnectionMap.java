package com.peigongdh.gameinner.map;

import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

public class GateConnectionMap {

    private static final Logger logger = LoggerFactory.getLogger(GateConnectionMap.class);

    private static ConcurrentHashMap<Long, GateConnection> gateConnectionMap = new ConcurrentHashMap<>();

    private static GateConnection getGateConnection(ChannelHandlerContext ctx) {
        // FIXME: bug here when client left, NullPointerException
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

    public static GateConnection addGateConnection(ChannelHandlerContext ctx) {
        // FIXME: add again should remove one of before
        GateConnection conn = new GateConnection(ctx);

        if (gateConnectionMap.putIfAbsent(conn.getGateId(), conn) != null) {
            logger.error("duplicated gateId");
        }
        return conn;
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
