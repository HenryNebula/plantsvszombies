package com.plantsvszombies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Wallnut extends BasePlants {
    private Animation<TextureRegion> crack1Animation;
    private Animation<TextureRegion> crack2Animation;
    private float maxBlood = 200;

    public Wallnut(float x, float y, Stage s) {
        super(x, y, s, false);
        blood = maxBlood;

        loadAnimationFromFiles(generateFilesFromDirectory("wallnut"), 0.08f, true);
        crack1Animation = loadAnimationFromFiles(generateFilesFromDirectory("wallnut_crack1"), 0.1f, true);
        crack2Animation = loadAnimationFromFiles(generateFilesFromDirectory("wallnut_crack2"), 0.1f, true);
        setBoundaryPolygon(4);
    }

    @Override
    public void act(float dt) {
        super.act(dt);
        if (blood < maxBlood * 0.5) {
            if (blood < maxBlood * 0.25) {
                setAnimation(crack2Animation);
            }
            else setAnimation(crack1Animation);

        }
    }
}
