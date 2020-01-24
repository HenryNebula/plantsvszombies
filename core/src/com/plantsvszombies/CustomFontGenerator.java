package com.plantsvszombies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class CustomFontGenerator {
    private BitmapFont customFont;
    private FreeTypeFontGenerator.FreeTypeFontParameter fontParameters;

    CustomFontGenerator(int size, Color color, int borderWidth,
                        Color borderColor, boolean borderStraight) {
        fontParameters = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameters.size = size;
        fontParameters.color = color;
        fontParameters.borderWidth = borderWidth;
        fontParameters.borderColor = borderColor;
        fontParameters.borderStraight = borderStraight;
        fontParameters.minFilter = Texture.TextureFilter.Linear;
        fontParameters.magFilter = Texture.TextureFilter.Linear;

    }

    CustomFontGenerator(int size, Color color) {
        fontParameters = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameters.size = size;
        fontParameters.color = color;
        fontParameters.borderWidth = 2;
        fontParameters.borderColor = Color.BLACK;
        fontParameters.borderStraight = true;
        fontParameters.minFilter = Texture.TextureFilter.Linear;
        fontParameters.magFilter = Texture.TextureFilter.Linear;
    }

    CustomFontGenerator() {
        fontParameters = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameters.size = 10;
        fontParameters.color = Color.WHITE;
        fontParameters.borderWidth = 2;
        fontParameters.borderColor = Color.BLACK;
        fontParameters.borderStraight = true;
        fontParameters.minFilter = Texture.TextureFilter.Linear;
        fontParameters.magFilter = Texture.TextureFilter.Linear;
    }

    public BitmapFont getCustomFont(){
        FreeTypeFontGenerator fontGenerator =
                new FreeTypeFontGenerator(Gdx.files.internal("core/assets/OpenSans.ttf"));
        customFont = fontGenerator.generateFont(fontParameters);
        return customFont;
    }

}
