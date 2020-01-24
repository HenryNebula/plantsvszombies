package com.plantsvszombies;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

public class MyGdxGame extends Game {
    public enum LevelSetting {
        EASY, MIDDLE, HARD
    }

	private static Game game;
	private static LevelSetting levelSetting;

	public MyGdxGame() {
		game = this;
	}
	public void create()
	{
		InputMultiplexer im = new InputMultiplexer();
		Gdx.input.setInputProcessor( im );
		setActiveScreen( new MenuScreen());
	}

	@Override
	public void render() {
		super.render();
	}

	public static void setActiveScreen(BaseScreen s)
	{
		game.setScreen(s);
	}

	//default: core/assets/skin/uiskin.json
	public static Skin getSkin() {
		Skin skin =  new Skin(Gdx.files.internal("core/assets/skin/uiskin.json"));
		skin.getFont("default-font").getData().setScale(1.5f);
//		skin.get
		return skin;
    }


}
