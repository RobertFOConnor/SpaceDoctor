package com.yellowbytestudios.spacedoctor.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.yellowbytestudios.spacedoctor.media.Assets;
import com.yellowbytestudios.spacedoctor.media.Fonts;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.cameras.OrthoCamera;

public class GUIManager {

    private OrthoCamera camera;
    private Array<SpacemanPlayer> players;

    // TIMER
    private long startTurnTime; // Timer variables.
    public static long duration;
    private long timeElapsed;

    private ShapeRenderer shapeRenderer;
    private Texture gui_display;

    private boolean isTimed;


    public GUIManager(Array<SpacemanPlayer> players, boolean isTimed) {
        camera = new OrthoCamera();
        camera.resize();
        this.players = players;
        this.isTimed = isTimed;

        //Initialize Timer
        duration = 30000;
        startTurnTime = System.nanoTime();

        shapeRenderer = new ShapeRenderer();
        gui_display = Assets.manager.get(Assets.GUI_DISPLAY, Texture.class);
    }

    public void update() {

        if(isTimed) {
            timeElapsed = duration - ((System.nanoTime() - startTurnTime) / 1000000);
        }
    }

    public void render(SpriteBatch sb) {

        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.identity();
        shapeRenderer.setColor(Color.RED);

        for(int i = 0; i < players.size; i++) {
            shapeRenderer.rect(215+(i*1425), MainGame.HEIGHT - 105, (players.get(i).getCurrGas() / 5) * 1.2f, 40);
        }
        shapeRenderer.end();

        sb.setProjectionMatrix(camera.combined);
        sb.begin();

        for(int i = 0; i < players.size; i++) {

            sb.draw(gui_display, 100+(i*1425), MainGame.HEIGHT - 200);
            Fonts.GUIFont.draw(sb, players.get(i).getMaxAmmo() + "/" + players.get(i).getCurrAmmo(), 225+(i*1425), MainGame.HEIGHT - 127);

            //Fonts.GUIFont.draw(sb, String.format("%07d", players.get(i).getCoins()), (MainGame.WIDTH - 250)+(i*1425), MainGame.HEIGHT - 70);
        }

        if(isTimed) {
            drawTimer(sb);
        }

        sb.end();
    }

    private void drawTimer(SpriteBatch sb) {
        Fonts.timerFont.draw(sb, "TIME", MainGame.WIDTH / 2 - 60, MainGame.HEIGHT - 30);

        String seconds;
        if ((timeElapsed / 1000) < 10) {
            Fonts.timerFont.setColor(Color.RED);
            seconds = "0" + (timeElapsed / 1000);
        } else {
            seconds = (timeElapsed / 1000) + "";
        }

        Fonts.timerFont.draw(sb, seconds + ":" + ((timeElapsed % 1000) / 10), MainGame.WIDTH / 2 - 80, MainGame.HEIGHT - 100);
        Fonts.timerFont.setColor(Color.WHITE);
    }

    public boolean timeIsUp() {
        return isTimed && timeElapsed < 0;
    }
}
