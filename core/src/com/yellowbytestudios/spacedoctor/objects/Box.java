package com.yellowbytestudios.spacedoctor.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by BobbyBoy on 09-Jan-16.
 */
public class Box extends Box2DSprite {

    public Box(Body body) {
        super(body);
        body.setUserData(this);
        texture = new Texture(Gdx.files.internal("box.png"));
        width = texture.getWidth();
        height = texture.getHeight();
    }
}