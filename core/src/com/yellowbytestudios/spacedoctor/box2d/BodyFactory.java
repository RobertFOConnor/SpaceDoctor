package com.yellowbytestudios.spacedoctor.box2d;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.yellowbytestudios.spacedoctor.game.objects.Box;
import com.yellowbytestudios.spacedoctor.game.objects.Door;
import com.yellowbytestudios.spacedoctor.game.objects.Enemy;
import com.yellowbytestudios.spacedoctor.game.objects.Exit;
import com.yellowbytestudios.spacedoctor.game.objects.PickUp;
import com.yellowbytestudios.spacedoctor.game.objects.Platform;
import com.yellowbytestudios.spacedoctor.mapeditor.MapManager;
import com.yellowbytestudios.spacedoctor.screens.GameScreen;

public class BodyFactory {

    private static float PPM = Box2DVars.PPM;

    public static Body createBody(World world, String bodyType) {

        if (bodyType.equals("PLAYER")) {

            // Create Body Definition object to define settings.
            BodyDef bdef = new BodyDef();
            bdef.type = BodyDef.BodyType.DynamicBody;

            bdef.fixedRotation = true;
            bdef.linearVelocity.set(0f, 0f);
            bdef.position.set(2, 5);

            // Create Body object to hold fixtures.
            Body body = world.createBody(bdef);


            // Create circle for players head.
            CircleShape circleShape = new CircleShape();
            circleShape.setRadius(51 / PPM);
            circleShape.setPosition(new Vector2(0, 22 / PPM));

            // Create Fixture Definition for head collision.
            FixtureDef fdef = new FixtureDef();
            fdef.shape = circleShape;
            fdef.filter.categoryBits = Box2DVars.BIT_PLAYER;
            fdef.filter.maskBits = Box2DVars.BIT_WALL | Box2DVars.BIT_BOX | Box2DVars.BIT_PICKUP | Box2DVars.BIT_ENEMY | Box2DVars.BIT_SPIKE;
            body.createFixture(fdef).setUserData("player");


            // Create box for players torso.
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(32 / PPM, 44 / PPM, new Vector2(0, -30 / PPM), 0);

            // Create Fixture Definition for torso collision box.
            fdef.shape = shape;
            fdef.restitution = 0.03f;
            fdef.filter.categoryBits = Box2DVars.BIT_PLAYER;
            fdef.filter.maskBits = Box2DVars.BIT_WALL | Box2DVars.BIT_BOX | Box2DVars.BIT_PICKUP | Box2DVars.BIT_ENEMY | Box2DVars.BIT_SPIKE;
            body.createFixture(fdef).setUserData("player");


            // Create box for players foot.
            shape = new PolygonShape();
            shape.setAsBox(30 / PPM, 20 / PPM, new Vector2(0, -60 / PPM), 0);

            // Create Fixture Definition for foot collision box.
            fdef.shape = shape;
            fdef.isSensor = true;
            fdef.filter.categoryBits = Box2DVars.BIT_PLAYER;
            fdef.filter.maskBits = Box2DVars.BIT_WALL | Box2DVars.BIT_EXIT | Box2DVars.BIT_BOX | Box2DVars.BIT_ENEMY | Box2DVars.BIT_SPIKE;

            // create player foot fixture
            body.createFixture(fdef).setUserData("foot");
            shape.dispose();

            return body;


        } else if (bodyType.equals("BULLET")) {

            BodyDef bdef = new BodyDef();
            bdef.type = BodyDef.BodyType.DynamicBody;

            bdef.fixedRotation = true;

            // create body from bodydef
            Body body = world.createBody(bdef);

            // create box shape for bullet.
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(50 / PPM, 1 / PPM);

            // create fixturedef for bullet.
            FixtureDef cfdef = new FixtureDef();
            cfdef.shape = shape;
            cfdef.filter.categoryBits = Box2DVars.BIT_BULLET;
            cfdef.filter.maskBits = Box2DVars.BIT_WALL | Box2DVars.BIT_BOX | Box2DVars.BIT_ENEMY;
            body.createFixture(cfdef).setUserData("bullet");
            body.setGravityScale(0f);
            shape.dispose();

            return body;

        } else {
            return null;
        }
    }

    public static Array<Box> createBoxes(World world, TiledMap tm) {

        MapLayer ml = tm.getLayers().get("boxes");
        Array<Box> boxes = new Array<Box>();

        if (ml == null) return new Array<Box>();

        float width = 48 / PPM;
        float height = 48 / PPM;

        for (MapObject mo : ml.getObjects()) {

            BodyDef cdef = new BodyDef();
            cdef.type = BodyDef.BodyType.DynamicBody;
            Vector2 pos = getMapObjectPos(mo);
            cdef.position.set(pos.x + width, pos.y + height);

            Body body = world.createBody(cdef);

            FixtureDef cfdef = new FixtureDef();
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(width, height);
            cfdef.shape = shape;
            cfdef.density = 0.1f;
            cfdef.filter.categoryBits = Box2DVars.BIT_BOX;
            cfdef.filter.maskBits = Box2DVars.BIT_PLAYER | Box2DVars.BIT_BULLET | Box2DVars.BIT_WALL | Box2DVars.BIT_BOX | Box2DVars.BIT_ENEMY;

            body.createFixture(cfdef).setUserData("box");
            shape.dispose();

            Box b = new Box(body);
            boxes.add(b);
            body.setUserData(b);
        }
        return boxes;
    }


    public static Exit createExits(World world, TiledMap tm) {

        MapLayer ml = tm.getLayers().get("exits");
        float width = 50 / PPM;
        float height = 100 / PPM;
        Exit exit = null;

        if (ml == null) { //CUSTOM MAP - TEMP
            exit = createExitBody(world, MapManager.exitX, MapManager.exitY, width, height);
        } else {
            for (MapObject mo : ml.getObjects()) {
                Vector2 pos = getMapObjectPos(mo);
                exit = createExitBody(world, pos.x + (width), pos.y + (height), width, height);
            }
        }
        return exit;
    }


    private static Exit createExitBody(World world, float x, float y, float width, float height) {
        BodyDef cdef = new BodyDef();
        cdef.type = BodyDef.BodyType.StaticBody;
        cdef.position.set(x, y);

        Body body = world.createBody(cdef);

        FixtureDef cfdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width, height);
        cfdef.shape = shape;
        cfdef.isSensor = true;
        cfdef.filter.categoryBits = Box2DVars.BIT_EXIT;
        cfdef.filter.maskBits = Box2DVars.BIT_PLAYER;

        body.createFixture(cfdef).setUserData("door");
        shape.dispose();

        Exit d = new Exit(body);
        body.setUserData(d);
        return d;
    }


    public static Array<PickUp> createPickups(World world, TiledMap tm) {

        MapLayer ml = tm.getLayers().get("pickups");
        Array<PickUp> pickups = new Array<PickUp>();

        if (ml == null) return new Array<PickUp>();

        float width = 35 / PPM;
        float height = 35 / PPM;

        for (MapObject mo : ml.getObjects()) {

            BodyDef cdef = new BodyDef();
            cdef.type = BodyDef.BodyType.StaticBody;
            String type = mo.getProperties().get("type", String.class);
            if (type == null) { //Default to gas pickup if no type is specified.
                type = "coin";
            }

            Vector2 pos = getMapObjectPos(mo);
            cdef.position.set(pos.x + width, pos.y + height);

            Body body = world.createBody(cdef);

            FixtureDef cfdef = new FixtureDef();
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(width, height);
            cfdef.shape = shape;
            cfdef.isSensor = true;
            cfdef.filter.categoryBits = Box2DVars.BIT_PICKUP;
            cfdef.filter.maskBits = Box2DVars.BIT_PLAYER;

            body.createFixture(cfdef).setUserData("pickup");
            shape.dispose();

            PickUp p = new PickUp(body, type);
            pickups.add(p);
            body.setUserData(p);
        }
        return pickups;
    }

    public static Array<Platform> createPlatforms(World world, TiledMap tm) {

        Array<Platform> platforms = new Array<Platform>();

        MapLayer ml = tm.getLayers().get("platforms");
        if (ml == null) return new Array<Platform>();


        for (MapObject mo : ml.getObjects()) {

            Rectangle rectangle = ((RectangleMapObject) mo).getRectangle();

            String type = mo.getProperties().get("type", String.class);
            float width = rectangle.width / PPM;
            float height = rectangle.height / PPM;

            BodyDef cdef = new BodyDef();
            cdef.type = BodyDef.BodyType.KinematicBody;
            Vector2 pos = getMapObjectPos(mo);
            cdef.position.set(pos.x + (width / 2), pos.y + (height / 2));

            Body body = world.createBody(cdef);

            PolygonShape bodyShape = new PolygonShape();
            bodyShape.setAsBox(width / 2, height / 2);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.density = 1f;
            fixtureDef.shape = bodyShape;
            fixtureDef.filter.categoryBits = Box2DVars.BIT_SPIKE;
            fixtureDef.filter.maskBits = Box2DVars.BIT_PLAYER;

            body.createFixture(fixtureDef).setUserData("wall");
            Platform p = new Platform(body, type);
            p.setLimit(Float.parseFloat(mo.getProperties().get("distance", String.class)));
            body.setUserData(p);
            platforms.add(p);
            bodyShape.dispose();
        }

        return platforms;
    }


    public static Array<Door> createDoors(World world, TiledMap tm) {

        Array<Door> doors = new Array<Door>();

        MapLayer ml = tm.getLayers().get("doors");
        if (ml == null) return new Array<Door>();


        for (MapObject mo : ml.getObjects()) {

            Rectangle rectangle = ((RectangleMapObject) mo).getRectangle();

            float width = rectangle.width / PPM;
            float height = rectangle.height / PPM;

            BodyDef cdef = new BodyDef();
            cdef.type = BodyDef.BodyType.KinematicBody;
            Vector2 pos = getMapObjectPos(mo);
            cdef.position.set(pos.x + (width / 2), pos.y + (height / 2));

            Body body = world.createBody(cdef);

            PolygonShape bodyShape = new PolygonShape();
            bodyShape.setAsBox(width / 2, height / 2);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.density = 1f;
            fixtureDef.shape = bodyShape;
            fixtureDef.filter.categoryBits = Box2DVars.BIT_WALL;
            fixtureDef.filter.maskBits = Box2DVars.BIT_PLAYER;

            body.createFixture(fixtureDef).setUserData("wall");
            Door d = new Door(body, width*Box2DVars.PPM, height*Box2DVars.PPM);
            body.setUserData(d);
            doors.add(d);
            bodyShape.dispose();
        }

        return doors;
    }


    public static Array<Enemy> createEnemies(World world, TiledMap tm) {

        MapLayer ml = tm.getLayers().get("enemies");
        Array<Enemy> enemies = new Array<Enemy>();

        if (GameScreen.customMap != null) {
            for (MapManager.DraggableObject mapObject : MapManager.enemyList) {
                Vector2 pos = new Vector2(mapObject.getPos().x / 100, mapObject.getPos().y / 100);
                enemies.add(createEnemy(world, pos));
            }
        } else {
            for (MapObject mo : ml.getObjects()) {
                enemies.add(createEnemy(world, getMapObjectPos(mo)));
            }
        }
        return enemies;
    }

    private static Enemy createEnemy(World world, Vector2 pos) {

        float width = 51 / PPM;
        float height = 75 / PPM;

        BodyDef cdef = new BodyDef();
        cdef.type = BodyDef.BodyType.DynamicBody;
        cdef.position.set(pos.x + width, pos.y + height);
        cdef.fixedRotation = true;

        Body body = world.createBody(cdef);

        FixtureDef cfdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width, height);
        cfdef.shape = shape;
        cfdef.density = 5f;
        cfdef.filter.categoryBits = Box2DVars.BIT_ENEMY;
        cfdef.filter.maskBits = Box2DVars.BIT_PLAYER | Box2DVars.BIT_BULLET | Box2DVars.BIT_WALL | Box2DVars.BIT_SPIKE | Box2DVars.BIT_BOX | Box2DVars.BIT_ENEMY;

        body.createFixture(cfdef).setUserData("enemy");
        shape.dispose();

        Enemy e = new Enemy(body);
        body.setUserData(e);
        return e;
    }

    private static Vector2 getMapObjectPos(MapObject mo) {
        return new Vector2((mo.getProperties().get("x", Float.class) / PPM), (mo.getProperties().get("y", Float.class) / PPM));
    }
}
