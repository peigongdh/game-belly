package com.peigongdh.gameinner.browserquest.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Group {

    private String id;

    private ConcurrentHashMap<String, Entity> entities;

    private List<Entity> incoming;

    private List<String> playerIds;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ConcurrentHashMap<String, Entity> getEntities() {
        return entities;
    }

    public void setEntities(ConcurrentHashMap<String, Entity> entities) {
        this.entities = entities;
    }

    public List<Entity> getIncoming() {
        return incoming;
    }

    public void setIncoming(List<Entity> incoming) {
        this.incoming = incoming;
    }

    public List<String> getPlayerIds() {
        return playerIds;
    }

    public void setPlayerIds(List<String> playerIds) {
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
