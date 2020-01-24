package com.plantsvszombies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.util.Date;

public class SwimmerZombie extends BaseZombie {
    private Animation<TextureRegion> swimAnimation;

    public SwimmerZombie(float x, float y, Stage s) {
        super(x, y, s, "swimmer");
        setZombieSpeed(10);
        setZombieScale(0.9f);
        setBoundaryPolygon(4);
        swimAnimation = loadAnimationFromFiles(generateFilesFromDirectory("swimmer_swim"), 0.1f, true);
    }


    @Override
    protected void move(float dt) {
        plantMet = null;
        //1200-275
        if (getX() > 230 && getX() < 1200-275) {
            for (BaseActor plant: BaseActor.getAliveList(getStage(), "BasePlants")) {
                if (getRow() == plant.getRow() && overlaps(plant)){
                    plantMet = (BasePlants) plant;
                    break;
                }
            }
            swim(dt);
        }
        else walk(dt);
        eat(dt);
    }

    private void swim(float dt) {
        if (plantMet != null) return;
        if (!this.getAnimation().getKeyFrame(0.0f).getTexture().toString().contains("_swim")){
            setAnimation(swimAnimation);
            Sound jumpInWater = getSoundFromFile("zombie_entering_water");
            jumpInWater.play(0.3f, 1.3f, 0);
        }
        resetSpeedAffectedByBullet(dt);
        applyPhysics((float) 1.0/60);
    }
}
