package com.peigongdh.gamegate.map;

import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

public class ClientConnectionMap {

    private static final Logger logger = LoggerFactory.getLogger(ClientConnectionMap.class);

    private static ConcurrentHashMap<Long, ClientConnection> clientConnectionMap = new ConcurrentHashMap<>();

    private static ClientConnection getClientConnection(ChannelHandlerContext ctx) {
        Long clientId = ctx.channel().attr(ClientConnection.CLIENT_ID).get();
        return getClientConnection(clientId);
    }

    private static ClientConnection getClientConnection(Long clientId) {
        ClientConnection conn = clientConnectionMap.get(clientId);
        if (conn != null) {
            return conn;
        } else {
            logger.error("clientId {} not found in clientConnectionMap", clientId);
        }
        return null;
    }

    public static void addClientConnection(ChannelHandlerContext ctx) {
        // FIXME: login again should remove one of before
        ClientConnection conn = new ClientConnection(ctx);

        if (clientConnectionMap.putIfAbsent(conn.getClientId(), conn) != null) {
            logger.error("duplicated clientId");
        }
    }

    public static void removeClientConnection(ChannelHandlerContext ctx) {
        ClientConnection conn = getClientConnection(ctx);
        if (null == conn) {
            logger.error("conn not found when remove clientConnection");
            return;
        }
        Long clientId = conn.getClientId();
        if (clientConnectionMap.remove(clientId) != null) {
            // TODO
        } else {
            logger.error("clientId {} not found in clientConnectionMap", clientId);
        }
    }
}
