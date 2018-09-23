package com.peigongdh.gameinner;

import com.peigongdh.gameinner.browserquest.domain.World;
import com.peigongdh.gameinner.client.RegisterClient;
import com.peigongdh.gameinner.server.InnerServer;

public class GameInnerStart {

    public static void main(String[] args) {
        World world = new World(1, 1000);
        world.run("world_server.json");
        new Thread(() -> new InnerServer().start(world)).start();
        new Thread(() -> new RegisterClient().start()).start();
    }
}
