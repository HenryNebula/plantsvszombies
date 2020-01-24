package com.plantsvszombies;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class Sunflower extends BasePlants {

    private Sunlight sunlight = null;
    private final float CD = 5.0f;
    private float bloomColdTime = CD;
    private boolean hasSunlight = true;

    public Sunflower(float x, float y, Stage s) {
        super(x, y, s, false);
        blood = 200;
        loadAnimationFromFiles(generateFilesFromDirectory("sunflower"), 0.08f, true);
    }

    private void generateSunlight(float dt) {
        if (!hasDropTarget()) return;

        if (hasSunlight) {
            bloomColdTime -= dt;
        }

        if (bloomColdTime < 0 && hasSunlight ) {
            sunlight = new Sunlight(0,0, this.getStage(), false);
            sunlight.setSunflowerParent(this);
            sunlight.centerAtActor(this);
            bloomColdTime = CD;
            hasSunlight = false;
        }

        if (sunlight == null) {
            hasSunlight = true;
        }
    }

    public void removeSunlight () {
        sunlight = null;
    }

    @Override
    public void act(float dt) {
        super.act(dt);
        generateSunlight(dt);
    }


}
