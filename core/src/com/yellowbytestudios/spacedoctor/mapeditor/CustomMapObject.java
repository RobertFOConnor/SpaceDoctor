package com.yellowbytestudios.spacedoctor.mapeditor;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Robert on 03/16/16.
 */
public class CustomMapObject {

    private Vector2 pos;
    private String id;

    public CustomMapObject(String id, Vector2 pos) {
        this.id = id;
        this.pos = pos;
    }

    public Vector2 getPos() {
        return pos;
    }

    public String getId() {
        return id;
    }
}
