package com.peigongdh.gameinner;

import com.peigongdh.gameinner.client.RegisterClient;
import com.peigongdh.gameinner.server.InnerServer;

public class GameInnerStart {

    public static void main(String[] args) {
        new Thread(() -> new InnerServer().start()).start();
        new Thread(() -> new RegisterClient().start()).start();
    }
}
