package com.yellowbytestudios.spacedoctor.mapeditor;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.yellowbytestudios.spacedoctor.box2d.Box2DVars;


public class CustomMap extends TiledMap {

    private String name;

    private int[][] tileArray;

    private Vector2 exitPos = new Vector2(15, 4);
    private Vector2 startPos = new Vector2(3, 4);

    private Array<CustomMapObject> enemyArray = new Array<CustomMapObject>();
    private Array<CustomMapObject> itemArray = new Array<CustomMapObject>();

    public CustomMap() {

    }


    public CustomMap(String name, TiledMap tm) {
        this.name = name;

        TiledMapTileLayer layer = (TiledMapTileLayer) tm.getLayers().get(0);
        tileArray = new int[layer.getWidth()][layer.getHeight()];

        //Cycle through layer from tiledmap and create a 2d array of the tile ids.
        for (int x = 0; x < layer.getWidth(); x++) {
            for (int y = 0; y < layer.getHeight(); y++) {

                if (layer.getCell(x, y) != null) {
                    tileArray[x][y] = layer.getCell(x, y).getTile().getId();
                } else {
                    tileArray[x][y] = -1;
                }
            }
        }

        exitPos = new Vector2(MapManager.exitX * Box2DVars.PPM, MapManager.exitY * Box2DVars.PPM);
        startPos = new Vector2(MapManager.startX * Box2DVars.PPM, MapManager.startY * Box2DVars.PPM);

        for (MapManager.DraggableObject enemy : MapManager.enemyList) {
            enemyArray.add(new CustomMapObject(enemy.getId(), enemy.getCenter()));
        }

        for (MapManager.DraggableObject item : MapManager.itemList) {
            itemArray.add(new CustomMapObject(item.getId(), item.getCenter()));
        }
    }

    public String getName() {
        return name;
    }

    public int[][] loadMap() {
        return tileArray;
    }

    public Vector2 getStartPos() {
        return startPos;
    }

    public Vector2 getExitPos() {
        return exitPos;
    }

    public Array<CustomMapObject> getEnemyArray() {
        return enemyArray;
    }

    public Array<CustomMapObject> getItemArray() {
        return itemArray;
    }


}
