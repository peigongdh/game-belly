package com.peigongdh.gameinner.browserquest.domain;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.peigongdh.gameinner.browserquest.util.Util;

import java.io.*;
import java.util.*;
import java.util.function.Consumer;

public class Map {

    private boolean loaded;

    private int width;

    private int height;

    private List<Integer> collisions;

    private List<MobAreaConfig> mobAreas;

    private List<ChestAreaConfig> chestAreas;

    private List<StaticChestConfig> staticChests;

    private java.util.Map<Integer, String> staticEntities;

    private java.util.Map<Integer, Checkpoint> checkpoints;

    private List<Checkpoint> startingAreas;

    private int zoneWidth;

    private int zoneHeight;

    private int groupWidth;

    private int groupHeight;

    private Consumer<Map> readyCallback;

    private java.util.Map<String, List<Position>> connectedGroups;

    private int[][] grid;

    private String filePath;

    public Map(String filePath) {
        this.filePath = filePath;
        this.loaded = false;
    }

    public void initMap() {
        Scanner s = new Scanner(new InputStreamReader(Map.class.getClassLoader().getResourceAsStream(this.filePath)));
        String mapJson = s.hasNext() ? s.next() : "";
        JSONObject mapObject = JSONObject.parseObject(mapJson);
        this.width = Integer.parseInt(mapObject.getString("width"));
        this.height = Integer.parseInt(mapObject.getString("height"));
        Object collisionsArray = mapObject.get("collisions");
        this.collisions = JSON.parseArray(collisionsArray.toString(), Integer.class);
        Object mobAreaArray = mapObject.getJSONArray("roamingAreas");
        this.mobAreas = JSON.parseArray(mobAreaArray.toString(), MobAreaConfig.class);
        Object chestAreasArray = mapObject.getJSONArray("chestAreas");
        this.chestAreas = JSON.parseArray(chestAreasArray.toString(), ChestAreaConfig.class);
        Object staticChestsArray = mapObject.getJSONArray("staticChests");
        this.staticChests = JSON.parseArray(staticChestsArray.toString(), StaticChestConfig.class);
        Object staticEntitiesMap = mapObject.get("staticEntities");
        this.staticEntities = JSON.parseObject(staticEntitiesMap.toString(), new TypeReference<HashMap<Integer, String>>() {
        });

        Object doorsArray = mapObject.getJSONArray("doors");
        List<DoorConfig> doors = JSON.parseArray(doorsArray.toString(), DoorConfig.class);
        Object checkpointsArray = mapObject.getJSONArray("checkpoints");
        List<CheckpointConfig> checkpoints = JSON.parseArray(checkpointsArray.toString(), CheckpointConfig.class);

        // zone groups
        this.zoneWidth = 28;
        this.zoneHeight = 12;
        this.groupWidth = this.width / this.zoneWidth;
        this.groupHeight = this.height / this.zoneHeight;

        this.initConnectedGroup(doors);
        this.initCheckpoints(checkpoints);

        if (null != this.readyCallback) {
            readyCallback.accept(this);
        }

        this.loaded = true;
    }

    public void onReady(Consumer<Map> callback) {
        this.readyCallback = callback;
    }

    public Position titleIndexToGridPostion(int titleNum) {
        titleNum -= 1;
        int x = this.getX(titleNum + 1, this.width);
        int y = titleNum / this.width;
        return new Position(x, y);
    }

    public int gridPositionToTileIndex(int x, int y) {
        return y * this.width + x + 1;
    }

    private int getX(int num, int w) {
        if (num == 0) {
            return 0;
        }
        return num % w == 0 ? w - 1 : num % w - 1;
    }

    public void generateCollisionGrid() {
        if (this.loaded) {
            int titleIndex = 0;
            java.util.Map<Integer, Integer> collisionsMap = new HashMap<>();
            for (int i = 0; i < this.collisions.size(); i++) {
                collisionsMap.put(this.collisions.get(i), i);
            }
            this.grid = new int[height][];
            for (int i = 0; i < this.height; i++) {
                this.grid[i] = new int[width];
                for (int j = 0; j < this.width; j++) {
                    if (null != collisionsMap.get(titleIndex)) {
                        this.grid[i][j] = 1;
                    } else {
                        this.grid[i][j] = 0;
                    }
                    titleIndex++;
                }
            }
        }
    }

    public boolean isOutOfBounds(int x, int y) {
        return x <= 0 || x >= this.width || y <= 0 || y >= this.height;
    }

    public boolean isColliding(int x, int y) {
        if (this.isOutOfBounds(x, y)) {
            return false;
        }
        return this.grid[y][x] == 1;
    }

    public void forEachGroup(Consumer<String> callback) {
        for (int i = 0; i < this.groupWidth; i++) {
            for (int j = 0; j < this.groupHeight; j++) {
                callback.accept(i + "-" + j);
            }
        }
    }

    private void initConnectedGroup(List<DoorConfig> doors) {
        this.connectedGroups = new HashMap<>();
        doors.forEach(doorConfig -> {
            String groupId = this.getGroupIdFromPosition(doorConfig.getX(), doorConfig.getY());
            String connectedGroupId = this.getGroupIdFromPosition(doorConfig.getTx(), doorConfig.getTy());
            Position connectedPosition = this.groupIdToGroupPosition(connectedGroupId);
            List<Position> positionList = connectedGroups.get(groupId);
            if (connectedGroups.get(groupId) != null) {
                positionList.add(connectedPosition);
                connectedGroups.put(groupId, positionList);
            } else {
                List<Position> list = new ArrayList<>();
                list.add(connectedPosition);
                connectedGroups.put(groupId, list);
            }
        });
    }

    private String getGroupIdFromPosition(int x, int y) {
        int gx = (x - 1) / this.zoneWidth;
        int gy = (y - 1) / this.zoneHeight;
        return gx + "-" + gy;
    }

    private Position groupIdToGroupPosition(String connectedGroupId) {
        String[] posArray = connectedGroupId.split("-");
        return new Position(Integer.parseInt(posArray[0]), Integer.parseInt(posArray[1]));
    }

    private void initCheckpoints(List<CheckpointConfig> checkpointsConfig) {
        this.checkpoints = new HashMap<>();
        this.startingAreas = new ArrayList<>();
        checkpointsConfig.forEach(config -> {
            Checkpoint checkpoint = new Checkpoint(config.getId(), config.getX(), config.getY(), config.getW(), config.getH());
            checkpoints.put(checkpoint.getId(), checkpoint);
            if (config.getS() == 1) {
                startingAreas.add(checkpoint);
            }
        });
    }

    private List<Position> getAdjacentGroupPositions(String id) {
        Position pos = this.groupIdToGroupPosition(id);
        List<Position> groupPositionList = new ArrayList<>();
        // surrounding groups
        groupPositionList.add(new Position(pos.getX() - 1, pos.getY() - 1));
        groupPositionList.add(new Position(pos.getX(), pos.getY() - 1));
        groupPositionList.add(new Position(pos.getX() + 1, pos.getY() - 1));
        groupPositionList.add(new Position(pos.getX() - 1, pos.getY()));
        groupPositionList.add(new Position(pos.getX(), pos.getY()));
        groupPositionList.add(new Position(pos.getX() + 1, pos.getY()));
        groupPositionList.add(new Position(pos.getX() - 1, pos.getY() + 1));
        groupPositionList.add(new Position(pos.getX(), pos.getY() + 1));
        groupPositionList.add(new Position(pos.getX() + 1, pos.getY() + 1));

        // groups connected via doors
        List<Position> positionList = this.connectedGroups.get(id);
        if (null != positionList) {
            // don't add a connected group if it's already part of the surrounding ones.
            positionList.forEach(position -> {
                if (Util.any(groupPositionList, groupPosition -> groupPosition.equals(position))) {
                    groupPositionList.add(position);
                }
            });
        }

        return Util.reject(groupPositionList, position -> position.getX() < 0 || position.getY() < 0 || pos.getX() >= groupWidth || position.getY() >= groupHeight);
    }

    public void forEachAdjacentGroup(String groupId, Consumer<String> callback) {
        if (null != groupId && !groupId.equals("")) {
            List<Position> groups = this.getAdjacentGroupPositions(groupId);
            groups.forEach(position -> callback.accept(position.toGroupId()));
        }
    }

    public Checkpoint getCheckpoint(int id) {
        return this.checkpoints.get(id);
    }

    public Position getRandomStartingPosition() {
        int nbAreas = this.startingAreas.size();
        int index = new Random().nextInt(nbAreas);
        Checkpoint area = this.startingAreas.get(index);
        return area.getRandomPosition();
    }

    /**
     * Test
     *
     * @param args
     */
    public static void main(String[] args) {
        Map map = new Map("world_server.json");
        map.initMap();
//        for (java.util.Map.Entry<Integer, String> entry : map.staticEntities.entrySet()) {
//            System.out.println(entry.getValue());
//        }
        map.generateCollisionGrid();
        System.out.println(Arrays.deepToString(map.grid));
    }
}

class MobAreaConfig {
    private int id;

    private int x;

    private int y;

    private int width;

    private int height;

    private String type;

    private int nb;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNb() {
        return nb;
    }

    public void setNb(int nb) {
        this.nb = nb;
    }
}

class ChestAreaConfig {
    private int x;

    private int y;

    private int w;

    private int h;

    private List<Integer> i;

    private int tx;

    private int ty;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public List<Integer> getI() {
        return i;
    }

    public void setI(List<Integer> i) {
        this.i = i;
    }

    public int getTx() {
        return tx;
    }

    public void setTx(int tx) {
        this.tx = tx;
    }

    public int getTy() {
        return ty;
    }

    public void setTy(int ty) {
        this.ty = ty;
    }
}

class StaticChestConfig {
    private int x;

    private int y;

    private List<Integer> i;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public List<Integer> getI() {
        return i;
    }

    public void setI(List<Integer> i) {
        this.i = i;
    }
}

class DoorConfig {
    private int x;

    private int y;

    private int p;

    private int tcx;

    private int tcy;

    private String to;

    private int tx;

    private int ty;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getP() {
        return p;
    }

    public void setP(int p) {
        this.p = p;
    }

    public int getTcx() {
        return tcx;
    }

    public void setTcx(int tcx) {
        this.tcx = tcx;
    }

    public int getTcy() {
        return tcy;
    }

    public void setTcy(int tcy) {
        this.tcy = tcy;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getTx() {
        return tx;
    }

    public void setTx(int tx) {
        this.tx = tx;
    }

    public int getTy() {
        return ty;
    }

    public void setTy(int ty) {
        this.ty = ty;
    }
}

class CheckpointConfig {
    private int id;

    private int x;

    private int y;

    private int w;

    private int h;

    private int s;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getS() {
        return s;
    }

    public void setS(int s) {
        this.s = s;
    }
}