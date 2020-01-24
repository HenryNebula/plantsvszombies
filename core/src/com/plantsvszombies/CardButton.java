package com.plantsvszombies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class CardButton extends TextButton{

    private TextButton.TextButtonStyle cardButtonStyle =  new TextButton.TextButtonStyle();
    private int sunlightDemand;
    private String buttonRealText;

    private void initStyle(String textureName) {
        Texture   upButtonTex   = new Texture(
                Gdx.files.internal(String.format("core/assets/cards/%s.png", textureName)));
        Texture   downButtonTex   = new Texture(
                Gdx.files.internal(String.format("core/assets/cards/%s_gray.png", textureName)));

        CustomFontGenerator customFontGenerator = new CustomFontGenerator(10, Color.WHITE);

        cardButtonStyle.up = new TextureRegionDrawable(upButtonTex);
        cardButtonStyle.down = new TextureRegionDrawable(downButtonTex);
        cardButtonStyle.disabled = new TextureRegionDrawable(downButtonTex);
        cardButtonStyle.font      = customFontGenerator.getCustomFont();
    }

    CardButton(String name) {
        super(name.split("_")[0], new TextButtonStyle(null, null, null, new BitmapFont()));
        String[] characteristics = name.split("_");

        buttonRealText = characteristics[0];
        if (characteristics.length != 1) {
            sunlightDemand = Integer.valueOf(characteristics[1]);
        }
        else sunlightDemand = 0;

        initStyle(buttonRealText);
        setStyle(cardButtonStyle);

    }

    public TextButton.TextButtonStyle getCardButtonStyle(){
        return cardButtonStyle;
    }

    public int getSunlightDemand() {
        return sunlightDemand;
    }

    public String getButtonRealText() {
        return buttonRealText;
    }
}
