package com.plantsvszombies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class MapArea extends DropTargetActor{
    private String successFile = "core/assets/success.png";
    private String failureFile = "core/assets/failure.png";
    private boolean waterArea;
    private boolean lilypad=false;

    public MapArea(float x, float y, Stage s, boolean waterArea) {
        super(x, y, s);
        this.waterArea = waterArea;
        loadScaledTexture(successFile, true);
        setBoundaryPolygon(4);
    }

    public void showSuccessTexture() {
        loadScaledTexture(successFile, false);
    }

    public void showFailureTexture() {
        loadScaledTexture(failureFile, false);
    }

    private void setOldSize(float[] size) {
        setWidth(size[0]);
        setHeight(size[1]);
    }

    private float[] getOldSize() {
        float[] size = new float[2];
        size[0] = getWidth();
        size[1] = getHeight();
        return size;
    }
    private void loadScaledTexture(String texName, boolean firstVisit){
        float[] oldSize;
        if (! firstVisit) {
            clearActions();
            oldSize = getOldSize();
        }
        else {
            oldSize = new float[2];
        }
        Animation<TextureRegion> animation =
                loadAnimationFromFiles(new String[] {texName}, 0.0f, false);
        setAnimation(animation);
        setOpacity(0.0f);

        if(! firstVisit){
            addAction(Actions.sequence(Actions.fadeIn(0.5f), Actions.fadeOut(0.5f)));
            setOldSize(oldSize);
        }
    }

    public boolean checkWaterArea() {
        return waterArea;
    }

    public boolean hasLilyPad() {
        return lilypad;
    }

    public void setLilypad(boolean lilypad) {
        this.lilypad = lilypad;
    }

    @Override
    public boolean isTargetable() {
        return super.isTargetable();
    }
}
