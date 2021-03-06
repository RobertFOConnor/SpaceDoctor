package com.yellowbytestudios.spacedoctor.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.controllers.XBoxController;
import com.yellowbytestudios.spacedoctor.effects.SoundManager;
import com.yellowbytestudios.spacedoctor.media.Assets;
import com.yellowbytestudios.spacedoctor.media.Fonts;
import com.yellowbytestudios.spacedoctor.screens.BackgroundManager;
import com.yellowbytestudios.spacedoctor.screens.Screen;
import com.yellowbytestudios.spacedoctor.tween.AnimationManager;
import com.yellowbytestudios.spacedoctor.tween.SpriteButton;
import com.yellowbytestudios.spacedoctor.tween.SpriteText;
import com.yellowbytestudios.spacedoctor.utils.Metrics;

/**
 * Created by BobbyBoy on 16-Jan-16.
 */
public class TitleScreen extends Screen {

    private BackgroundManager bg;
    private SpriteButton character, title;
    private SpriteText continueMessage, versionCode;
    private boolean advancing = false;

    private final Vector2 charStartPos = new Vector2(2000, -800);

    @Override
    public void create() {
        super.create();
        bg = new BackgroundManager();

        character = new SpriteButton(Assets.CHARACTER, charStartPos);
        title = new SpriteButton(Assets.TITLE, new Vector2(-1100, 350));
        continueMessage = new SpriteText(MainGame.languageFile.get("TOUCH_TO_CONTINUE").toUpperCase(), Fonts.timerFont);
        continueMessage.setPosition(400, -100);

        versionCode = new SpriteText(MainGame.languageFile.get("VERSION_CODE").toUpperCase(), Fonts.smallFont);
        versionCode.setPosition(70, Metrics.HEIGHT + 100);

        AnimationManager.applyAnimation(continueMessage, 400, 90);
        AnimationManager.applyAnimation(versionCode, 70, Metrics.HEIGHT - 50);
        AnimationManager.applyAnimation(character, 1080, -250);
        AnimationManager.applyAnimation(title, 70, 350);
        AnimationManager.startAnimation();
    }

    @Override
    public void update(float step) {

        bg.update();

        if (MainGame.hasControllers) {
            if (MainGame.controller.getButton(XBoxController.BUTTON_START)) {
                advanceScreen();
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            advanceScreen();

        } else if (Gdx.input.justTouched()) {
            advanceScreen();
        }
    }

    private void advanceScreen() {
        if (!advancing) {
            AnimationManager.applyAnimation(continueMessage, 400, -100);
            AnimationManager.applyAnimation(versionCode, 70, Metrics.HEIGHT + 100);
            AnimationManager.applyAnimation(character, charStartPos.x, charStartPos.y);
            AnimationManager.applyExitAnimation(title, -1100, 350, new MainMenuScreen());
            AnimationManager.startAnimation();

            SoundManager.play(Assets.BUTTON_CLICK);
            advancing = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {

        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        bg.render(sb);
        character.draw(sb);
        title.draw(sb);
        continueMessage.draw(sb);
        versionCode.draw(sb);
        sb.end();
    }

    @Override
    public void goBack() {
        Gdx.app.exit();
    }
}
