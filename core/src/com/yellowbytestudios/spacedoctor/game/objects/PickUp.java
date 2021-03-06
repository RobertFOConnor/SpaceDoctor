package com.yellowbytestudios.spacedoctor.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.yellowbytestudios.spacedoctor.media.Assets;
import com.yellowbytestudios.spacedoctor.game.gui.GUIManager;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.game.player.SpacemanPlayer;
import com.yellowbytestudios.spacedoctor.box2d.Box2DVars;
import com.yellowbytestudios.spacedoctor.effects.SoundManager;
import com.yellowbytestudios.spacedoctor.screens.GameScreen;

import box2dLight.PointLight;

/**
 * Created by BobbyBoy on 10-Jan-16.
 */
public class PickUp extends Box2DSprite {

    private String type = "";
    private com.brashmonkey.spriter.Player spriter;
    private PointLight light;

    public PickUp(Body body, String type) {
        super(body);
        this.type = type;
        float scale = 0.85f;

        if (type.equals("gas")) {
            spriter = MainGame.spriterManager.getSpiter("gas_pickup", "default", scale);
        } else if (type.equals("ammo")) {
            spriter = MainGame.spriterManager.getSpiter("ammo_pickup", "default", scale);
        } else if (type.equals("time")) {
            spriter = MainGame.spriterManager.getSpiter("time_pickup", "default", scale);
        } else {
            spriter = MainGame.spriterManager.getSpiter("coin", "default", 0.8f);
        }
    }

    public void render(SpriteBatch sb) {
        spriter.setPosition((int) (posX * Box2DVars.PPM), (int) (posY * Box2DVars.PPM));
        spriter.update();
        MainGame.spriterManager.draw(spriter);
    }

    public void activate(SpacemanPlayer player) {

        if (type.equals("ammo")) {
            player.getGun().setAmmo(player.getGun().getAmmo()+10);
        } else if (type.equals("gas")) {
            player.setCurrGas(player.getCurrGas() + 250);
        } else if (type.equals("time")) {
            GUIManager.duration += 15000;
        } else {
            player.setCoins(player.getCoins()+1);
            GameScreen.particleManager.addCoinEffect((int) (posX * Box2DVars.PPM), (int) (posY * Box2DVars.PPM));
        }
        SoundManager.play(Assets.PICKUP_SOUND);
    }

    public void setLight(PointLight light) {
        this.light = light;
    }

    public PointLight getLight() {
        return light;
    }
}