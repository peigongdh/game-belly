package com.peigongdh.gamegate;

import com.peigongdh.gamegate.client.RegisterClient;
import com.peigongdh.gamegate.server.WebSocketServer;

public class GameGateBootStart {

    public static void main(String[] args) {
        new Thread(new WebSocketServer()).start();
        new Thread(new RegisterClient()).start();
//        new WebSocketServer().start();
//        new RegisterClient().start();
    }
}
