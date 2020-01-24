package com.plantsvszombies;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class SnowPea extends PeaShooter {
    public SnowPea(float x, float y, Stage s, String textureName) {
        super(x, y, s, textureName);
    }

    protected Bullet makeBullet() {
        Bullet snowBullet =  new Bullet(0,0,this.getStage(), "snowpeabullet");
        snowBullet.setBulletEffect(0.5f, 5, 2.0f);
        return snowBullet;
    }

}
