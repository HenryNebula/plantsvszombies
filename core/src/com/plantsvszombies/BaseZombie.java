package com.plantsvszombies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class BaseZombie extends BaseActor {
    protected float speedDecreaseDuration = -Float.MAX_VALUE;
    protected float normalSpeed;
    protected float CD = 1.0f;
    protected float damage = 20;
    protected float coolingTime = 0.0f;
    protected BasePlants plantMet = null;

    protected Animation<TextureRegion> eatAnimation;
    protected Animation<TextureRegion> walkAnimation;

    protected Sound appearSound;
    protected Sound eatSound;

    public BaseZombie(float x, float y, Stage s, String zombieName) {
        super(x, y, s);

        walkAnimation = loadAnimationFromFiles(generateFilesFromDirectory(zombieName), 0.1f, true);
        eatAnimation = loadAnimationFromFiles(generateFilesFromDirectory(zombieName + "_eat"), 0.1f, true);

        appearSound = getSoundFromFile(zombieName);
        eatSound = getSoundFromFile("zombie_eat");

        appearSound.play(0.2f);
    }

    public void setZombieScale(float xScale, float yScale) {
        setScale(xScale, yScale);
        setBoundaryPolygon(4);
    }

    public void setZombieScale(float xyScale) {
        setScale(xyScale);
        setBoundaryPolygon(4);
    }

    public void setZombieSpeed(float speed) {
        this.speed = speed;
        normalSpeed = speed;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public void setCD(float CD) {
        this.CD = CD;
    }

    protected String[] generateFilesFromDirectory(String directory) {
        return super.generateFilesFromDirectory(directory, "core/assets/zombie/%s/");
    }

    public void hitByBullet (Bullet bullet) {
        if (! bullet.isAlive()) return;
        Bullet.BulletEffect bulletEffect = bullet.getBulletEffect();
        blood -= bulletEffect.getDamage();
        if (speedDecreaseDuration < 0 && speed == normalSpeed) {
            speedDecreaseDuration = bulletEffect.getDuration();
            speed *= (1.0f - bulletEffect.getDecreaseEffect());
        }
    }

    protected void resetSpeedAffectedByBullet(float dt){
        if (speedDecreaseDuration > 0) {
            speedDecreaseDuration -= dt;
        }
        else {
            speedDecreaseDuration = -Float.MAX_VALUE;
            speed = normalSpeed;
        }
        setSpeed(-speed);
    }

    protected void walk(float dt) {
        if (plantMet != null) return;
        changeToAnimation("walk");
        resetSpeedAffectedByBullet(dt);
        applyPhysics(dt);
    }

    protected void eat(float dt) {
        if (plantMet != null) {
            coolingTime -= dt;
        }
        if (coolingTime < 0) {
            changeToAnimation("eat");
            coolingTime = CD;
            if (plantMet != null) {
                eatSound.play(0.1f);
                plantMet.hitByZombie(damage);
            }
        }
    }

    protected void move(float dt) {
        plantMet = null;
        for (BaseActor plant: BaseActor.getAliveList(getStage(), "BasePlants")) {
            if (getRow() == plant.getRow() && overlaps(plant)){
                    plantMet = (BasePlants) plant;
                    break;
                }
            }
        walk(dt);
        eat(dt);
    }

    private void changeToAnimation(String animationName) {
        if (animationName.contains("eat")) {
            setAnimation(eatAnimation);
        }
        else {
            setAnimation(walkAnimation);
        }
    }

    @Override
    public void act(float dt) {
        super.act(dt);
        if (isAlive())
            move(dt);
    }
}
