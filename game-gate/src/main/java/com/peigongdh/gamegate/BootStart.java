package com.peigongdh.gamegate;

import com.peigongdh.gamegate.server.WebSocketServer;

public class BootStart {

    public static void main(String[] args) {
        new WebSocketServer().start();
    }
}
