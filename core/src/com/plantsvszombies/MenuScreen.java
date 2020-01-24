package com.plantsvszombies;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MenuScreen extends BaseScreen
{
    private MyGdxGame.LevelSetting levelSetting;
    private ButtonGroup buttonGroup;

    private Music bgm;
    public void initialize()
    {
        BaseActor ocean = new BaseActor(0,0, mainStage);
        ocean.loadTexture( "core/assets/Logo.jpg" );
        ocean.setSize(1200,600);

        BaseActor start = new BaseActor(0,0, mainStage);
        start.loadTexture( "core/assets/message-start.png" );
        start.centerAtPosition(550,90);


        Skin skin = MyGdxGame.getSkin();
        CheckBox easyButton = new CheckBox("EASY", skin);
        CheckBox middleButton = new CheckBox("MIDDLE", skin);
        CheckBox hardButton = new CheckBox("HARD", skin);


        easyButton.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                Gdx.graphics.setContinuousRendering(easyButton.isChecked());
            }
        });

        middleButton.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                Gdx.graphics.setContinuousRendering(middleButton.isChecked());
            }
        });

        hardButton.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                Gdx.graphics.setContinuousRendering(hardButton.isChecked());
            }
        });


        buttonGroup = new ButtonGroup(easyButton, middleButton, hardButton);
        buttonGroup.setMaxCheckCount(1);
        buttonGroup.setMinCheckCount(1);
        buttonGroup.setUncheckLast(true);
        buttonGroup.setChecked("EASY");

//        for (Object textButton: buttonGroup.getButtons()) {
//            ((TextButton) textButton).addListener(
//                    new ButtonInputListener(((TextButton) textButton).getLabel().toString()));
//        }

        Table table = new Table();
        table.add(easyButton);
        table.add(middleButton).padLeft(40);
        table.add(hardButton).padLeft(40);
        table.setPosition(560,175);
        mainStage.addActor(table);

        bgm = BaseActor.getMusicFromFile("startmusic");
        bgm.setVolume(0.2f);
        bgm.setLooping(true);
        bgm.play();
    }

    private class ButtonInputListener extends InputListener{
        private String buttonName;

        ButtonInputListener(String buttonName) {
            this.buttonName = buttonName;
        }
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            for (MyGdxGame.LevelSetting level : MyGdxGame.LevelSetting.values()){
                if (level.toString().equals(buttonName.toUpperCase())) {
                    levelSetting = level;
                    break;
                }
            }
            return false;
        }
    }

    public void update(float dt)
    {
        if (Gdx.input.isKeyPressed(Keys.S)){
            for (MyGdxGame.LevelSetting level : MyGdxGame.LevelSetting.values()){
                if (buttonGroup.getChecked() == null) break;
                else {
                    if (level.toString().equals(
                            ((TextButton) buttonGroup.getChecked()).getLabel().getText().toString())) {
                        levelSetting = level;
                        break;
                    }
                }
            }
            bgm.stop();
            MyGdxGame.setActiveScreen( new LevelScreen(levelSetting) );
        }

    }
}
