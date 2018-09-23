package com.peigongdh.gameinner.handler;

import com.peigongdh.gameinner.browserquest.domain.Player;
import com.peigongdh.gameinner.browserquest.domain.World;
import com.peigongdh.gameinner.map.GateConnection;
import com.peigongdh.gameinner.map.GateConnectionMap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class InnerHandler extends SimpleChannelInboundHandler<String> {

    private World world;

    private Player player;

    public InnerHandler(World world) {
        this.world = world;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        GateConnection conn = GateConnectionMap.addGateConnection(ctx);
        this.player = new Player(conn.getGateId().toString(), ctx, world);
        world.getConnectCallback().accept(this.player);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) {
        this.player.onClientMessage(s);
        this.world.updatePopulation(0);
        ctx.writeAndFlush(s);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        GateConnectionMap.removeGateConnection(ctx);
        this.player.onClientClose();
        this.player = null;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        GateConnectionMap.removeGateConnection(ctx);
        this.player.onClientClose();
        this.player = null;
        cause.printStackTrace();
        ctx.close();
    }
}
