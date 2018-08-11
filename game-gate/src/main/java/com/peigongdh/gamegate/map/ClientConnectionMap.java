package com.peigongdh.gamegate.map;

import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class ClientConnectionMap {

    private static final Logger logger = LoggerFactory.getLogger(ClientConnectionMap.class);

    public static ConcurrentHashMap<Long, ClientConnection> clientConnectionMap = new ConcurrentHashMap<>();

    public static ClientConnection getClientConnection(ChannelHandlerContext ctx) {
        // Long clientId = ctx.channel().attr("");
        return null;
    }

    public static ClientConnection getClinetConnection(Long clientId) {
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
        ClientConnection clientConnection = new ClientConnection(ctx);

        if (clientConnectionMap.putIfAbsent(clientConnection.getClientId(), clientConnection) != null) {
            logger.error("duplicated clientId");
        }
    }

    public static void removeClientConnection(ChannelHandlerContext ctx) {
        // ClientConnection clientConnection =
    }
}