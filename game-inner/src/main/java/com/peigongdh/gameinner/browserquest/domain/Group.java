package com.peigongdh.gameinner.browserquest.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Group {

    private String id;

    private ConcurrentHashMap<Integer, Entity> entities;

    private List<Entity> incoming;

    private List<Integer> playerIds;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ConcurrentHashMap<Integer, Entity> getEntities() {
        return entities;
    }

    public void setEntities(ConcurrentHashMap<Integer, Entity> entities) {
        this.entities = entities;
    }

    public List<Entity> getIncoming() {
        return incoming;
    }

    public void setIncoming(List<Entity> incoming) {
        this.incoming = incoming;
    }

    public List<Integer> getPlayerIds() {
        return playerIds;
    }

    public void setPlayerIds(List<Integer> playerIds) {
        this.playerIds = playerIds;
    }

    public Group(String id) {
        this.id = id;
        this.entities = new ConcurrentHashMap<>();
        this.incoming = new ArrayList<>();
        this.playerIds = new ArrayList<>();
    }

    // TODO, remove all overwrite hashcode & equals
}
