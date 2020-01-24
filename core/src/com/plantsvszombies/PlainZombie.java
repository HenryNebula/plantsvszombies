package com.plantsvszombies;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class PlainZombie extends BaseZombie {
    public PlainZombie(float x, float y, Stage s) {
        super(x, y, s, "plain_zombie");
        setZombieSpeed(20);
        setZombieScale(0.8f);
        setBoundaryPolygon(4);
    }

    public PlainZombie(float x, float y, Stage s, String subName, float speed, float aniDuration, float scale) {
        super(x, y, s, subName);
        setZombieSpeed(speed);
        setZombieScale(scale);
        setBoundaryPolygon(4);
        walkAnimation.setFrameDuration(aniDuration);
    }

    public PlainZombie(float x, float y, Stage s, String subName, float speed) {
        super(x, y, s, subName);
        setZombieSpeed(speed);
        setZombieScale(0.8f);
        setBoundaryPolygon(4);
    }

    public PlainZombie(float x, float y, Stage s, String subName) {
        super(x, y, s, subName);
        setZombieSpeed(20);
        setZombieScale(0.8f);
        setBoundaryPolygon(4);
    }
}
