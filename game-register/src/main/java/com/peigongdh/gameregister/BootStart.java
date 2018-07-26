package com.peigongdh.gameregister;


import com.peigongdh.gameregister.server.RegisterServer;

public class BootStart {

    public static void main(String[] args) {
        new RegisterServer().start();
    }
}
