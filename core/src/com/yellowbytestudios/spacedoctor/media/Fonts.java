package com.yellowbytestudios.spacedoctor.media;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class Fonts {

    public static BitmapFont GUIFont, timerFont, largeFont;

    public static void load() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/basic_font.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 45;
        GUIFont = generator.generateFont(parameter);

        parameter.size = 60;
        timerFont = generator.generateFont(parameter);

        parameter.size = 80;
        largeFont = generator.generateFont(parameter);
    }

    public static void dispose() {
        GUIFont.dispose();
        timerFont.dispose();
        largeFont.dispose();
    }

    public static float getWidth(BitmapFont f, String s) {
        final GlyphLayout layout = new GlyphLayout(f, s);
        return layout.width / 2;
    }
}
