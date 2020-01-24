package com.plantsvszombies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class Bullet extends BaseActor {
    private float speed = 500.0f;

    protected BulletEffect bulletEffect = new BulletEffect();

    public Bullet(float x, float y, Stage s) {
        super(x, y, s);
        loadAnimationFromFiles(new String[]{"core/assets/plants/bullet/peabullet.gif"}, 1.0f, false);
        this.setScale(1.0f, 1.0f);
        setBoundaryPolygon(8);
    }

    public Bullet(float x, float y, Stage s, String textureName) {
        super(x, y, s);
        loadAnimationFromFiles(new String[]{String.format("core/assets/plants/bullet/%s.gif", textureName)}, 1.0f, false);
        this.setScale(1.0f, 1.0f);
        setBoundaryPolygon(8);
    }

    public BulletEffect getBulletEffect() {
        return bulletEffect;
    }

    public void setBulletEffect(float decreaseEffect, float damage, float duration) {
        bulletEffect.decreaseEffect = decreaseEffect;
        bulletEffect.damage = damage;
        bulletEffect.duration = duration;
    }

    private void fly() {
        setSpeed(speed);
        accelerateAtAngle(0);
        applyPhysics((float) 1.0/60);
    }

    public void setGrid(int row, int col) {
        setRow(row);
        setCol(col);
    }

    @Override
    public void act(float dt) {
        super.act(dt);
        fly();
    }

    public class BulletEffect {
        private float decreaseEffect = 0.0f;
        private float damage = 10.0f;
        private float duration = 0.0f;
        private Sound bulletSound;

        BulletEffect() {
            setBulletEffect(0.0f, 20.0f, 0.0f);
        }

        public void setBulletEffect(float decreaseEffect, float damage, float duration){
            this.decreaseEffect = decreaseEffect;
            this.damage = damage;
            this.duration = duration;

            if (decreaseEffect != 0) {
                bulletSound = getSoundFromFile("splat_snow");
            }
            else bulletSound = getSoundFromFile("splat");
        }

        public float getDuration() {
            return duration;
        }

        public float getDecreaseEffect() {
            return decreaseEffect;
        }

        public float getDamage() {
            return damage;
        }

        public void playHitSound() {
            bulletSound.play(0.5f);
        }
    }
}
