package com.peigongdh.gameregister;


import com.peigongdh.gameregister.server.RegisterServer;

public class GameRegisterBootStart {

    public static void main(String[] args) {
        new RegisterServer().start();
    }
}
