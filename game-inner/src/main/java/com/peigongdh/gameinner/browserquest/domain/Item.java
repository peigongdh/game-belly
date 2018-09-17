package com.peigongdh.gameinner.browserquest.domain;

import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class Item extends Entity {

    private boolean isStatic;

    private boolean isFromChest;

    private Timer blinkTimer;

    private Timer deSpawnTimer;

    private Consumer<Item> respawnCallback;

    public Item(int id, int kind, int x, int y) {
        super(id, "item", kind, x, y);
        this.isStatic = false;
        this.isFromChest = false;
    }

    // FIXME:
    public void handleDeSpawn(int beforeBlinkDelay, Consumer<Item> blinkCallback, int blinkingDuration, Consumer<Item> deSpawnCallback) {
        Item self = this;
        this.blinkTimer = new Timer();
        blinkTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                blinkCallback.accept(self);
                deSpawnTimer = new Timer();
                deSpawnTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        deSpawnCallback.accept(self);
                    }
                }, blinkingDuration);
            }
        }, beforeBlinkDelay);
    }

    public void destroy() {
        if (null != this.blinkTimer) {
            this.blinkTimer.cancel();
            this.blinkTimer = null;
        }
        if (null != this.deSpawnTimer) {
            this.deSpawnTimer.cancel();
            this.deSpawnTimer = null;
        }
        if (this.isStatic) {
            this.scheduleReSpawn(30000);
        }
    }

    // FIXME:
    public void scheduleReSpawn(int delay) {
        Item self = this;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                respawnCallback.accept(self);
            }
        }, delay);
    }

    public void onReSpawn(Consumer<Item> callback) {
        this.respawnCallback = callback;
    }

    /**
     * TEST lambda
     * FIXME: remove
     */

    public static void main(String[] args) {
        new Item(1, 0, 0, 0).handleDeSpawn(
                10000,
                item -> {
                    System.out.println("blinkCallback: " + item.getId());
                },
                4000,
                item -> {
                    System.out.println("deSpawnCallback: " + item.getId());
                }
        );
    }
}
