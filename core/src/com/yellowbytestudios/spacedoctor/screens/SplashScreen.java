package com.yellowbytestudios.spacedoctor.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.cameras.OrthoCamera;
import com.yellowbytestudios.spacedoctor.effects.SoundManager;
import com.yellowbytestudios.spacedoctor.media.Assets;
import com.yellowbytestudios.spacedoctor.media.Fonts;

public class SplashScreen implements Screen {

    private OrthoCamera camera;
    private String percentage;

    public SplashScreen() {
        camera = new OrthoCamera();
        camera.resize();
        Fonts.load();
        Assets.load();
    }

    @Override
    public void create() {
        camera = new OrthoCamera();
        camera.resize();
    }

    @Override
    public void update(float dt) {
        camera.update();
        percentage = ((int) (Assets.manager.getProgress() * 100) + "%");
    }

    @Override
    public void render(SpriteBatch sb) {

        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        Fonts.GUIFont.setColor(Color.BLACK);
        Fonts.GUIFont.draw(sb, percentage, MainGame.WIDTH / 2 - 30, MainGame.HEIGHT / 2);
        Fonts.GUIFont.setColor(Color.WHITE);
        sb.end();

        if (Assets.update()) { // DONE LOADING. SHOW TITLE SCREEN.

            SoundManager.setMusic(Assets.MAIN_THEME);

            if (MainGame.TEST_MODE) {
                ScreenManager.setScreen(new GameScreen(5));
            } else {
                ScreenManager.setScreen(new com.yellowbytestudios.spacedoctor.screens.menu.TitleScreen());
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.resize();
    }

    @Override
    public void dispose() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void goBack() {

    }
}
