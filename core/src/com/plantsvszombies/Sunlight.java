package com.plantsvszombies;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class Sunlight extends BaseActor {
    private float fallingSpeed = 100.0f;
    private Sunflower sunflowerParent = null;
    private boolean canFly;
    private float lifetime = 5.0f;
    private float addAmount = 50f;

    public Sunlight(float x, float y, Stage s, boolean fly) {
        super(x, y, s);
        speed = 300.0f;
        canFly = fly;

        loadAnimationFromFiles(generateFilesFromDirectory("sunlight", "core/assets/plants/%s/"),
                0.08f, true);
        setScale(0.7f);
        setBoundaryRectangle();

        addListener(new InputListener (){

            public boolean touchDown(InputEvent event, float eventOffsetX, float eventOffsetY, int pointer, int button)
            {
                if (lifetime > 0) {
                    moveToCollector();
                    lifetime = -1.0f;
                }
                return false; // returning true indicates other touch methods are called
            }
        });
    }

    private void moveToCollector()
    {
        lifetime = Float.MAX_VALUE;
        float collectorX = 213;
        float collectorY = 530;

        float flyTime = Vector2.dst(collectorX, collectorY, this.getCenterX(), this.getCenterY()) / speed;
        addAction(Actions.scaleTo(0.5f, 0.5f, flyTime));
        addAction( Actions.moveTo(collectorX, collectorY, flyTime, Interpolation.pow3) );
        this.addAction(Actions.after(Actions.fadeOut(0.2f)));
    }

    public void setSunflowerParent(Sunflower sunflower){
        if (!canFly){
            sunflowerParent = sunflower;
        }
    }

    private void falling(float dt) {
        lifetime -= dt;
        if (canFly) {
            if (this.getY() > 600 || lifetime < 0) {
                return;
            }

            this.setSpeed(fallingSpeed);
            this.setMotionAngle(270);
            this.applyPhysics(dt);
        }
        // for static sunlight on sunflowers
        else {
            if (lifetime < 0)
                sunflowerParent.removeSunlight();
            }
    }

    public float getAddAmount() {
        return addAmount;
    }

    public void setAddAmount(int sun) {
        addAmount = sun;
    }

    @Override
    public void act(float dt) {
        super.act(dt);
        falling(dt);
    }

    @Override
    public boolean isAlive() {
        return lifetime > 0;
    }
}
