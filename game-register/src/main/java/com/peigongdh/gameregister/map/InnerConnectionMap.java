package com.peigongdh.gameregister.map;

import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

public class InnerConnectionMap {

    private static final Logger logger = LoggerFactory.getLogger(InnerConnectionMap.class);

    public static ConcurrentHashMap<Long, InnerConnection> innerConnectionMap = new ConcurrentHashMap<>();

    private static InnerConnection getInnerConnection(ChannelHandlerContext ctx) {
        Long innerId = ctx.channel().attr(InnerConnection.INNER_ID).get();
        return getInnerConnection(innerId);
    }

    private static InnerConnection getInnerConnection(Long innerId) {
        InnerConnection conn = innerConnectionMap.get(innerId);
        if (conn != null) {
            return conn;
        } else {
            logger.error("innerId {} not found in InnerConnectionMap", innerId);
        }
        return null;
    }

    public static void addInnerConnection(ChannelHandlerContext ctx) {
        // FIXME: add again should remove one of before
        InnerConnection conn = new InnerConnection(ctx);

        if (innerConnectionMap.putIfAbsent(conn.getInnerId(), conn) != null) {
            logger.error("duplicated innerId");
        }
    }

    public static void removeInnerConnection(ChannelHandlerContext ctx) {
        InnerConnection conn = getInnerConnection(ctx);
        if (null == conn) {
            logger.error("conn not found when remove InnerConnection");
            return;
        }
        Long innerId = conn.getInnerId();
        if (innerConnectionMap.remove(innerId) != null) {
            // TODO
        } else {
            logger.error("innerId {} not found in InnerConnectionMap", innerId);
        }
    }
}
