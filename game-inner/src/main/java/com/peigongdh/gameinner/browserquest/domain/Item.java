package com.peigongdh.gameinner.browserquest.domain;

import java.util.Timer;
import java.util.TimerTask;

public class Item extends Entity {

    private boolean isStatic;

    private boolean isFromChest;

    private Timer blinkTimer;

    private Timer deSpawnTimer;

    private Runnable respawnCallback;

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
    }

    public boolean isFromChest() {
        return isFromChest;
    }

    public void setFromChest(boolean fromChest) {
        isFromChest = fromChest;
    }

    public Timer getBlinkTimer() {
        return blinkTimer;
    }

    public void setBlinkTimer(Timer blinkTimer) {
        this.blinkTimer = blinkTimer;
    }

    public Timer getDeSpawnTimer() {
        return deSpawnTimer;
    }

    public void setDeSpawnTimer(Timer deSpawnTimer) {
        this.deSpawnTimer = deSpawnTimer;
    }

    public Runnable getRespawnCallback() {
        return respawnCallback;
    }

    public void setRespawnCallback(Runnable respawnCallback) {
        this.respawnCallback = respawnCallback;
    }

    Item(int id, int kind, int x, int y) {
        super(id, "item", kind, x, y);
        this.isStatic = false;
        this.isFromChest = false;
    }

    void handleDeSpawn(int beforeBlinkDelay, Runnable blinkCallback, int blinkingDuration, Runnable deSpawnCallback) {
        this.blinkTimer = new Timer();
        blinkTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                blinkCallback.run();
                deSpawnTimer = new Timer();
                deSpawnTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        deSpawnCallback.run();
                    }
                }, blinkingDuration);
            }
        }, beforeBlinkDelay);
    }

    @Override
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

    private void scheduleReSpawn(int delay) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                respawnCallback.run();
            }
        }, delay);
    }

    void onReSpawn(Runnable callback) {
        this.respawnCallback = callback;
    }

    /**
     * TEST lambda
     * FIXME: remove
     */

    public static void main(String[] args) {
        new Item(1, 0, 0, 0).handleDeSpawn(
                10000,
                () -> System.out.println("blinkCallback: "),
                4000,
                () -> System.out.println("deSpawnCallback: ")
        );
    }
}
