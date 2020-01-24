package com.plantsvszombies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class PeaShooter extends BasePlants {
    private final float CD = 2.0f;
    private float shootingColdTime = 0.0f;
    private boolean hasBullet = true;
    private Bullet bullet = null;

    protected Sound attackSound;

    public PeaShooter(float x, float y, Stage s) {
        super(x, y, s, false);
        loadAnimationFromFiles(generateFilesFromDirectory("peashooter"), 0.1f, true);
        setBoundaryPolygon(4);
        attackSound = getSoundFromFile("peashooter_attack");
    }

    public PeaShooter(float x, float y, Stage s, String textureName) {
        super(x, y, s, false);
        loadAnimationFromFiles(generateFilesFromDirectory(textureName), 0.1f, true);
        setBoundaryPolygon(4);
        attackSound = getSoundFromFile(textureName.toLowerCase() + "_attack");
    }

    private boolean checkEnemy() {
        for ( BaseActor zombie : BaseActor.getAliveList(this.getStage(), "BaseZombie") )
        {
            if (zombie.getRow() == this.getRow()) {
                return true;
            }
        }
        return false;
    }

    protected Bullet makeBullet() {
        return new Bullet(0,0,this.getStage());
    }

    private void onFire(float dt) {
        if (hasDropTarget())
            shootingColdTime -= dt;

        if (shootingColdTime < 0 && hasBullet && checkEnemy()) {
            attackSound.play(0.4f, 1.5f, 0.0f);
            bullet = makeBullet();
            bullet.centerAtActor(new BaseActor(this.getCenterX() + this.getWidth() / 4,
                        this.getCenterY() + this.getHeight()/4, this.getStage()));
            bullet.setGrid(this.getRow(), this.getCol());
            shootingColdTime = CD;
            hasBullet = false;
        }
        else {
            if (bullet != null) {
                if (! checkExistenceOfBullet()) {
                    bullet.clearActions();
                    bullet.addAction(Actions.fadeOut(0.1f));
                    bullet.addAction(Actions.after(Actions.removeActor()));
                    bullet = null;
                    hasBullet = true;
                }
            }
        }
    }

    private boolean checkExistenceOfBullet() {
        if (bullet.getX() > 1200) return false;
        for (BaseActor zombie: BaseActor.getAliveList(this.getStage(), "BaseZombie")) {
            if (bullet.getRow() == zombie.getRow() && bullet.getRow() != -1 && bullet.overlaps(zombie)){
                bullet.bulletEffect.playHitSound();
                ((BaseZombie) zombie).hitByBullet(bullet);
                return false;
            }
        }
        return true;
    }

    @Override
    public void act(float dt) {
        super.act(dt);
        onFire(dt);
    }
}
