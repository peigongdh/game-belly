package com.peigongdh.gameweb;

import com.peigongdh.gameweb.server.HttpStaticFileServer;

public class GameWebBootStart {

    public static void main(String[] args) {
        new HttpStaticFileServer().start();
    }
}
