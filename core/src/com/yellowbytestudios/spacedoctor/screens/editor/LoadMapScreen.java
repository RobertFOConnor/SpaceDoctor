package com.yellowbytestudios.spacedoctor.screens.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.cameras.OrthoCamera;
import com.yellowbytestudios.spacedoctor.controllers.XBox360Pad;
import com.yellowbytestudios.spacedoctor.mapeditor.CustomMap;
import com.yellowbytestudios.spacedoctor.media.Assets;
import com.yellowbytestudios.spacedoctor.media.Fonts;
import com.yellowbytestudios.spacedoctor.screens.BackgroundManager;
import com.yellowbytestudios.spacedoctor.screens.Screen;
import com.yellowbytestudios.spacedoctor.screens.ScreenManager;
import com.yellowbytestudios.spacedoctor.tween.AnimationManager;
import com.yellowbytestudios.spacedoctor.tween.SpriteButton;
import com.yellowbytestudios.spacedoctor.tween.SpriteText;

public class LoadMapScreen implements Screen {

    private OrthoCamera camera;
    private Vector2 touch;
    private BackgroundManager bg;
    private SpriteText title;
    private Array<LoadMapButton> mapButtons;
    private SpriteButton backButton;


    @Override
    public void create() {
        camera = new OrthoCamera();
        camera.resize();
        touch = new Vector2();

        bg = new BackgroundManager();
        title = new SpriteText("SELECT A MAP TO LOAD", Fonts.timerFont);
        title.centerText();

        setupMapButtons();

        backButton = new SpriteButton(Assets.GO_BACK, new Vector2(-150, 900));

        AnimationManager.applyAnimation(title, title.getX(), MainGame.HEIGHT - 60);
        AnimationManager.applyAnimation(backButton, 50, backButton.getY());
        AnimationManager.startAnimation();
    }

    private void setupMapButtons() {
        mapButtons = new Array<LoadMapButton>();

        float x = 150;
        float y = 640;

        if (MainGame.saveData.getMyMaps().size <= 4) {
            x += 560;
        }


        for (CustomMap cm : MainGame.saveData.getMyMaps()) {
            LoadMapButton lmb = new LoadMapButton(cm.getName(), new Vector2(x, y - MainGame.HEIGHT));
            mapButtons.add(lmb);
            AnimationManager.applyAnimation(lmb, x, y);

            y -= 180;
            if (mapButtons.size % 4 == 0) {
                x += 560;
                y = 640;
            }
        }
    }


    @Override
    public void update(float step) {
        camera.update();
        bg.update();

        if (MainGame.hasControllers) {
            if (MainGame.controller.getButton(XBox360Pad.BUTTON_A)) {
            }

        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {

        } else if (Gdx.input.justTouched()) {
            touch = camera.unprojectCoordinates(Gdx.input.getX(),
                    Gdx.input.getY());

            for (LoadMapButton lmb : mapButtons) {
                if (lmb.checkTouch(touch)) {
                    ScreenManager.setScreen(new MapEditorSplashScreen(new MapEditorScreen(MainGame.saveData.getMyMaps().get(mapButtons.indexOf(lmb, true)))));
                }
            }

            if (backButton.checkTouch(touch)) {
                goBack();
            }
        }
    }


    private void advanceScreen(final Screen s) {

        AnimationManager.applyAnimation(title, title.getX(), MainGame.HEIGHT + 100);
        for (LoadMapButton lb : mapButtons) {
            AnimationManager.applyAnimation(lb, lb.getX(), lb.getY() - MainGame.HEIGHT);
        }

        AnimationManager.applyExitAnimation(backButton, -150, 900, s);

        AnimationManager.startAnimation();
    }

    @Override
    public void render(SpriteBatch sb) {

        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        bg.render(sb);
        title.draw(sb);
        backButton.draw(sb);

        for (LoadMapButton lmb : mapButtons) {
            lmb.draw(sb);
        }
        sb.end();
    }

    private class LoadMapButton extends SpriteButton {

        private NinePatch bg;
        private String name;
        private int width = 500;
        private int height = 150;

        public LoadMapButton(String name, Vector2 pos) {
            super(Assets.BOX, pos);
            this.name = name;
            if (name == null) {
                this.name = "Map name here";
            }

            this.bg = new NinePatch(getTexture(), 40, 40, 40, 40);
            setRegionWidth(width);
            setRegionHeight(height);
        }

        @Override
        public void draw(Batch sb) {
            bg.draw(sb, getX(), getY(), width, height);
            Fonts.GUIFont.setColor(Color.BLACK);
            Fonts.GUIFont.draw(sb, name, getX() + 50, getY() + 90);
            Fonts.GUIFont.setColor(Color.WHITE);
        }
    }

    private class OverlayMenu {

        private String mapTitle;
        private float mapTitleX;
        private SpriteButton loadButton, deleteButton, copyButton;

        private LoadMapButton mapButton;

        public OverlayMenu(LoadMapButton mapButton) {
            this.mapButton = mapButton;

            //Setup title.
            mapTitle = mapButton.name;
            mapTitleX = MainGame.WIDTH - Fonts.getWidth(Fonts.GUIFont, mapTitle) / 2;

            //Setup load/delete/copy map buttons.
            loadButton = new SpriteButton(Assets.GO_BACK, new Vector2(150, 900));
            deleteButton = new SpriteButton(Assets.GO_BACK, new Vector2(150, 900));
            copyButton = new SpriteButton(Assets.GO_BACK, new Vector2(150, 900));
        }

        public void update() {

            if (Gdx.input.justTouched()) {
                touch = camera.unprojectCoordinates(Gdx.input.getX(),
                        Gdx.input.getY());

                if (loadButton.checkTouch(touch)) {
                    ScreenManager.setScreen(new MapEditorSplashScreen(new MapEditorScreen(MainGame.saveData.getMyMaps().get(mapButtons.indexOf(mapButton, true)))));
                }
            }
        }


        public void render(SpriteBatch sb) {
            Fonts.GUIFont.draw(sb, mapTitle, mapTitleX, 900);

            loadButton.draw(sb);
            deleteButton.draw(sb);
            copyButton.draw(sb);
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
        advanceScreen(new NewLoadScreen());
    }
}